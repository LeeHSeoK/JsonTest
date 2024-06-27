from fastapi import FastAPI, HTTPException, File, UploadFile
from fastapi.responses import JSONResponse
from pydantic import BaseModel
from openai import OpenAI
from dotenv import load_dotenv

import torch
import numpy as np
import pandas as pd
import torch.nn as nn

import requests
import os
import re
import base64

load_dotenv()

app = FastAPI()




# 현재 파일의 디렉토리 경로를 기준으로 경로 설정
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
data_path = os.path.join(BASE_DIR, 'like_user.csv')
model_path = os.path.join(BASE_DIR, 'autoencoder_model.pth')

# 추천 시스템 부분
# 데이터 및 모델 로드
data = pd.read_csv(data_path, encoding='euc-kr')
data['place_name'] = data['place_name'].str.strip()
user_item_matrix_df = data.pivot_table(index='user_id', columns='place_name', aggfunc='size', fill_value=0)
place_names = user_item_matrix_df.columns.tolist()
user_item_matrix = user_item_matrix_df.values

class Autoencoder(nn.Module):
    def __init__(self, num_items):
        super(Autoencoder, self).__init__()
        self.encoder = nn.Sequential(
            nn.Linear(num_items, 128),
            nn.ReLU(),
            nn.Linear(128, 64),
            nn.ReLU()
        )
        self.decoder = nn.Sequential(
            nn.Linear(64, 128),
            nn.ReLU(),
            nn.Linear(128, num_items),
            nn.Sigmoid()
        )
    
    def forward(self, x):
        encoded = self.encoder(x)
        decoded = self.decoder(encoded)
        return decoded

num_items = user_item_matrix.shape[1]
model = Autoencoder(num_items)
model.load_state_dict(torch.load(model_path))
model.eval()

def recommend_places(user_id, model, user_item_matrix, place_names, top_k=5):
    user_index = int(user_id.replace('user', '')) - 1
    user_vector = user_item_matrix[user_index]
    user_vector = torch.FloatTensor(user_vector).unsqueeze(0)
    
    with torch.no_grad():
        predicted_vector = model(user_vector).numpy().flatten()
    
    recommended_indices = np.argsort(predicted_vector)[-top_k:]
    recommended_titles = [place_names[i] for i in recommended_indices]
    
    return recommended_titles

@app.get("/recommend")
async def get_recommendations(userid: str):
    try:
        if not userid.startswith('user'):
            raise ValueError("Invalid user_id format")
        recommendations = recommend_places(userid, model, user_item_matrix, place_names)
        return recommendations
    except IndexError:
        raise HTTPException(status_code=404, detail="User not found")
    except ValueError as ve:
        raise HTTPException(status_code=400, detail=str(ve))





client = OpenAI()

# Get API key from environment variables
api_key = os.getenv('IMGBB_API_KEY')

# ImageBB
@app.post("/upload-image/")
async def upload_image(file: UploadFile = File(...)):
    try:
        # Read image file
        image_data = await file.read()
        
        # Prepare payload for ImageBB API
        upload_url = 'https://api.imgbb.com/1/upload'
        payload = {
            'key': api_key,
            'image': base64.b64encode(image_data).decode('utf-8'),
        }
        response = requests.post(upload_url, data=payload)
        
        # Process response
        if response.status_code == 200:
            data = response.json()
            return JSONResponse(content={"image_url": data['data']['url']})
        else:
            raise HTTPException(status_code=response.status_code, detail="Failed to upload image")
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))



class QueryRequest(BaseModel):
    question: str

class ImageQueryRequest(BaseModel):
    url: str
    message: str = None

# 최근 대화 내용을 저장하기 위한 리스트
recent_conversations = []
recent_image_conversations = []

def get_assistant(client: OpenAI, model: str, instructions: str, use_vector_store: bool = True):
    tools = [{"type": "file_search"}] if use_vector_store else []
    tool_resources = {"file_search": {"vector_store_ids": ["vs_mDKYrkA0JncCNc8QdJo4Ofs6"]}} if use_vector_store else {}
    return client.beta.assistants.create(
        instructions=instructions,
        temperature=1,
        model=model,
        tools=tools,
        tool_resources=tool_resources
    )

def get_image_assistant(client: OpenAI, model: str, instructions: str):
    tools = []
    tool_resources = {}
    return client.beta.assistants.create(
        instructions=instructions,
        temperature=1,
        model=model,
        tools=tools,
        tool_resources=tool_resources
    )

def get_thread(client: OpenAI):
    return client.beta.threads.create()

def add_message_to_thread(client: OpenAI, thread_id: str, message: str, role: str = "user"):
    client.beta.threads.messages.create(
        thread_id=thread_id,
        role=role,
        content=message,
    )

def create_run(client: OpenAI, thread_id: str, assistant_id: str):
    return client.beta.threads.runs.create(
        thread_id=thread_id,
        assistant_id=assistant_id
    )

def get_run(client: OpenAI, thread_id: str, run_id: str):
    return client.beta.threads.runs.retrieve(
        thread_id=thread_id,
        run_id=run_id
    )

def get_completed_run(client: OpenAI, thread_id: str, run_id: str):
    run = get_run(client, thread_id, run_id)
    while run.status == "queued" or run.status == "in_progress":
        run = get_run(client, thread_id, run_id)
    return run

def get_assistant_messages(client: OpenAI, thread_id: str):
    message_history = []
    thread_messages = client.beta.threads.messages.list(thread_id, order='desc')
    datas = thread_messages.data
    for data in datas:
        if data.role == 'assistant':
            for content in data.content:
                if content.type == 'text' and content.text.value:
                    message_history.append(f'{content.text.value}')
                elif content.type == 'image_file' and content.image_file.file_id:
                    message_history.append(f'{content.image_file.file_id}')
        else:
            break
    
    return message_history[::-1]

def delete_assistant(client: OpenAI, assistant_id: str):
    client.beta.assistants.delete(assistant_id)

def delete_thread(client: OpenAI, thread_id: str):
    client.beta.threads.delete(thread_id)

def remove_substrings_between(text, start_delim, end_delim):
    pattern = re.compile(f'{re.escape(start_delim)}.*?{re.escape(end_delim)}')
    return re.sub(pattern, '', text)

@app.post("/ask")
async def ask_question(query: QueryRequest):
    global recent_conversations

    # GPT-3.5 Assistant
    assistant_gpt35 = get_assistant(client, "gpt-3.5-turbo", 
        "You are an assistant that recommends travel destinations in Korea based on the attached file." +
        "You must respond in Korean language." +
        "Recommend only one travel destination. Also, explain the reason."
        ,
        use_vector_store=True
    )
    # GPT-4 Assistant
    assistant_gpt4o = get_assistant(client, "gpt-4o", 
        "Return only the name of the recommended location from the given sentence. It must be a word from the sentence." +
        "example: " +
        "input = '동촌유원지 근처에서 계속 머물고 싶은 카페인 인스퍼레이션디동촌유원지를 추천드립니다. 이 카페는 분위기가 좋고 휴식을 취하기에 안성맞춤인 곳입니다.'" +
        "output =  '인스퍼레이션디동촌유원지'",
        use_vector_store=False
    )
    
    thread = get_thread(client)
    
    if len(recent_conversations) > 0:
        for conversation in recent_conversations:
            add_message_to_thread(client=client, thread_id=thread.id, message=conversation, role="user")
    
    try:
        add_message_to_thread(client=client, thread_id=thread.id, message=query.question, role="user")
        run_gpt35 = create_run(client, thread.id, assistant_gpt35.id)
        run_gpt35 = get_completed_run(client, thread.id, run_gpt35.id)
        
        if run_gpt35.status == "completed":
            messages_gpt35 = get_assistant_messages(client, thread.id)
            response_gpt35 = "\n".join(messages_gpt35)
            
            # GPT-3.5의 응답을 GPT-4o로 전달
            add_message_to_thread(client=client, thread_id=thread.id, message=messages_gpt35[0], role="user")
            run_gpt4o = create_run(client, thread.id, assistant_gpt4o.id)
            run_gpt4o = get_completed_run(client, thread.id, run_gpt4o.id)
            
            if run_gpt4o.status == "completed":
                messages_gpt4o = get_assistant_messages(client, thread.id)
                response_gpt4o = "\n".join(messages_gpt4o)
                
                # 최근 대화 5개 저장
                recent_conversations.append(query.question)
                if len(recent_conversations) > 5:
                    recent_conversations.pop(0)
                
                # 두 응답을 모두 반환
                return remove_substrings_between(response_gpt35, '【', '】') + "\n\n" + "추천장소 바로가기 : " + response_gpt4o
                
            else:
                raise HTTPException(status_code=500, detail="GPT-4 응답에서 문제가 발생했습니다. 다시 시도해 주세요.")
        else:
            raise HTTPException(status_code=500, detail="GPT-3.5 응답에서 문제가 발생했습니다. 다시 시도해 주세요.")
    finally:
        delete_assistant(client, assistant_gpt35.id)
        delete_assistant(client, assistant_gpt4o.id)
        delete_thread(client, thread.id)

# @app.post("/ask/image")
# async def ask_image_question(query: ImageQueryRequest):
#     global recent_image_conversations

#     instructions = (
#         "You are an assistant that analyzes images to identify the location. " +
#         "You must respond in Korean language. " +
#         "Please check the given URL and tell us the location. " +
#         "The given URL is a place in South Korea." +
#         "Please recommend tourist attractions around this location."
#     )
    
#     assistant_image = get_image_assistant(client, "gpt-4o", instructions)

#     thread = get_thread(client)

#     try:
#         if len(recent_image_conversations) > 0:
#             for conversation in recent_image_conversations:
#                 add_message_to_thread(client=client, thread_id=thread.id, message=conversation, role="user")

#         # 이미지 URL과 메시지 추가
#         add_message_to_thread(client=client, thread_id=thread.id, message=f"Image URL: {query.url}\nMessage: {query.message or 'None'}", role="user")
        
#         run_image = create_run(client, thread.id, assistant_image.id)
#         run_image = get_completed_run(client, thread.id, run_image.id)
        
#         if run_image.status == "completed":
#             messages_image = get_assistant_messages(client, thread.id)
#             response_image = "\n".join(messages_image)
            
#             # 최근 이미지 관련 대화 5개 저장
#             recent_image_conversations.append(f"Image URL: {query.url}\nMessage: {query.message or 'None'}")
#             if len(recent_image_conversations) > 5:
#                 recent_image_conversations.pop(0)
            
#             return response_image
#         else:
#             # 추가 디버깅 정보 출력
#             raise HTTPException(status_code=500, detail=f"이미지 분석 응답에서 문제가 발생했습니다. 다시 시도해 주세요. Status: {run_image.status}")
#     except Exception as e:
#         # 예외 발생 시 로그 출력
#         raise HTTPException(status_code=500, detail=f"이미지 분석 중 예외가 발생했습니다: {str(e)}")
#     finally:
#         delete_assistant(client, assistant_image.id)
#         delete_thread(client, thread.id)

@app.post("/ask/image")
async def ask_image_question(query: ImageQueryRequest):
    global recent_image_conversations

    instructions = (
        "You are an assistant that analyzes images to identify the location. " +
        "You must respond in Korean language. " +
        "Please check the given URL and tell us the location. " +
        "The given URL is a place in South Korea." +
        "Please recommend tourist attractions around this location."
    )
    
    assistant_image = get_image_assistant(client, "gpt-4o", instructions)

    thread = get_thread(client)

    try:
        # URL이 변경되었는지 확인하고 리스트 비우기
        if recent_image_conversations and recent_image_conversations[-1]["url"] != query.url:
            recent_image_conversations = []

        if len(recent_image_conversations) > 0:
            for conversation in recent_image_conversations:
                add_message_to_thread(client=client, thread_id=thread.id, message=conversation["message"], role="user")

        # 이미지 URL과 메시지 추가
        new_conversation = {"url": query.url, "message": f"Image URL: {query.url}\nMessage: {query.message or 'None'}"}
        add_message_to_thread(client=client, thread_id=thread.id, message=new_conversation["message"], role="user")
        
        run_image = create_run(client, thread.id, assistant_image.id)
        run_image = get_completed_run(client, thread.id, run_image.id)
        
        if run_image.status == "completed":
            messages_image = get_assistant_messages(client, thread.id)
            response_image = "\n".join(messages_image)
            
            # 최근 이미지 관련 대화 5개 저장
            recent_image_conversations.append(new_conversation)
            if len(recent_image_conversations) > 5:
                recent_image_conversations.pop(0)
            
            return response_image
        else:
            # 추가 디버깅 정보 출력
            raise HTTPException(status_code=500, detail=f"이미지 분석 응답에서 문제가 발생했습니다. 다시 시도해 주세요. Status: {run_image.status}")
    except Exception as e:
        # 예외 발생 시 로그 출력
        raise HTTPException(status_code=500, detail=f"이미지 분석 중 예외가 발생했습니다: {str(e)}")
    finally:
        delete_assistant(client, assistant_image.id)
        delete_thread(client, thread.id)



if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="localhost", port=8000)
