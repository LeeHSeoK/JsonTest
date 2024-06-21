$(document).ready(function(){
    $("#send-button").click(function(){
        var userInput = $("#user-input").val();
        if(userInput.trim() !== ""){
            $("#chat-box").append("<div><strong>You:</strong> " + userInput + "</div>");
            $.post("/api/v1/gpt", { msg: userInput }, function(data) {
                // data 값을 "contentId_contentTypeID" 형식으로 받아와서 split
                var parts = data.split('_');
                var contentId = parts[0];
                var contentTypeId = parts[1];

                if (contentId !== "1") {
                    var button = '<button onclick="window.open(\'/searchresultinfo?contentId=' + contentId + '&contentTypeId=' + contentTypeId + '\', \'_blank\')">' + contentId + '</button>';
                    $("#chat-box").append("<div><strong>GPT:</strong> " + button + "</div>");
                } else {
                    $("#chat-box").append("<div><strong>GPT:</strong>미안해요. 원하는 결과를 가져오지 못했어요, 좀더 구체적인 내용을 입력해주세요!</div>");
                }
                console.log(data);
            }).fail(function() {
                alert("Error communicating with GPT");
            });
            $("#user-input").val("");
        }
    });
});