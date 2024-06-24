from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from openai import OpenAI
from dotenv import load_dotenv
import re

load_dotenv()

app = FastAPI()

client = OpenAI()

class QueryRequest(BaseModel):
    question: str

# 최근 대화 내용을 저장하기 위한 리스트
recent_conversations = []

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


if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="localhost", port=8000)
