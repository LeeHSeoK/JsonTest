
//////////////////////////////////////////////////////좋아요버튼
//좋아요 카운트 조회
async function getLikeInfo(bno) {
    try {
        const result = await axios.get(`/like/${bno}`);
        return result.data;
    } catch (error) {
        console.error("Error fetching like info:", error);
        throw error;
    }
}

// 좋아요 1증가
async function addCount(bno) {
    const result = await axios.post(`/like/${bno}`);
    return result.data;
}
// 좋아요 1감소
async function minusCount(bno) {
    const result = await axios.post(`/dislike/${bno}`);
    return result.data;
}

////////////////////////////////////////////////////////

async function get1(bno){
    const result = await axios.get(`/replies/list/${bno}`)  //비동기
    return result
}

async function getList({bno, page, size, goLast}){
    const result = await axios.get(`/replies/list/${bno}`,{params:{page,size}})

    if(goLast){
        const total = result.data.total
        const lastPage = parseInt(Math.ceil(total/size))
        return getList({bno:bno, page:lastPage, size:size})

    }
    return result.data
}

async function addReply(replyObj){
    const response = await axios.post(`/replies/`,replyObj)
    return response.data
}

async function getReply(rno){
    const response = await axios.get(`/replies/${rno}`)
    return response.data
}

async function modifyReply(replyObj){
    const response = await axios.put(`/replies/${replyObj.rno}`,replyObj)
    return response.data
}

async function removeReply(rno){
    const response = await axios.delete(`/replies/${rno}`)
    return response.data
}