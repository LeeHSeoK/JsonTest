//일휘 추가 코드
function checkImageCount() {
    var images = document.querySelectorAll('.image');
}

// 페이지 로드 시 이미지 개수 확인
window.onload = function () {
    checkImageCount();
};

//버튼 누를때 홈화면에 사진바꿔주는거.
function redirectToNaver(keyword) {
    const url = `http://localhost:8080/search?keyword=${encodeURIComponent(keyword)}`;
    // 페이지 이동
    window.location.href = url;
}
//허승씨 코드
document.addEventListener("DOMContentLoaded", function() {
    // 슬라이드 쇼 관련 기존 코드
    let slideIndex = 0;
    let slides = document.querySelectorAll(".slide");
    let slideContainer = document.querySelector(".slides");
    let pauseButton = document.getElementById("pauseButton");
    let isPaused = false;
    let slideInterval;

    function showSlides() {
        if (!isPaused) {
            slideIndex++;
            if (slideIndex >= slides.length) {
                slideIndex = 0;
            }
            slideContainer.style.transform = `translateX(-${slideIndex * 100}%)`;
            slideInterval = setTimeout(showSlides, 3000); // 3초마다 이미지 변경
        }
    }

    function plusSlides(n) {
        clearTimeout(slideInterval);
        slideIndex += n;
        if (slideIndex >= slides.length) {
            slideIndex = 0;
        }
        if (slideIndex < 0) {
            slideIndex = slides.length - 1;
        }
        slideContainer.style.transform = `translateX(-${slideIndex * 100}%)`;
        if (!isPaused) {
            slideInterval = setTimeout(showSlides, 3000);
        }
    }

    function togglePause() {
        isPaused = !isPaused;
        pauseButton.textContent = isPaused ? "Play" : "Pause";
        if (!isPaused) {
            showSlides();
        } else {
            clearTimeout(slideInterval);
        }
    }

    document.addEventListener("DOMContentLoaded", function() {
        showSlides();
    });

    // 버튼 슬라이드 기능 기존 코드
    const buttonRow = document.querySelector('.button-row');
    let isDown = false;
    let startX;
    let scrollLeft;

    buttonRow.addEventListener('mousedown', (e) => {
        isDown = true;
        buttonRow.classList.add('active');
        startX = e.pageX - buttonRow.offsetLeft;
        scrollLeft = buttonRow.scrollLeft;
    });

    buttonRow.addEventListener('mouseleave', () => {
        isDown = false;
        buttonRow.classList.remove('active');
    });

    buttonRow.addEventListener('mouseup', () => {
        isDown = false;
        buttonRow.classList.remove('active');
    });

    buttonRow.addEventListener('mousemove', (e) => {
        if (!isDown) return;
        e.preventDefault();
        const x = e.pageX - buttonRow.offsetLeft;
        const walk = (x - startX) * 2; // 스크롤 속도 증가
        buttonRow.scrollLeft = scrollLeft - walk;
    });

    // AIchat 관련 코드
    const aichatBtn = document.getElementById('aichat-btn');
    const chatWindow = document.getElementById('chat-window');
    const mainContent = document.getElementById('main');

    aichatBtn.addEventListener('click', toggleChat);

    function toggleChat() {
        if (chatWindow.style.display === 'none' || chatWindow.style.display === '') {
            chatWindow.style.display = 'flex';
            mainContent.style.marginRight = '30%'; // 오른쪽 여백 추가
        } else {
            chatWindow.style.display = 'none';
            mainContent.style.marginRight = '0'; // 오른쪽 여백 제거
        }
    }

    window.separateChat = function() {
        const chatWindowHTML = chatWindow.outerHTML;
        const newWindow = window.open('', '', 'width=400,height=600');
        newWindow.document.write(`
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Separated Chat</title>
                <link rel="stylesheet" href="chat.css">
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        height: 100vh;
                        overflow: hidden;
                    }
                    .chat-window {
                        width: 100%;
                        height: 100%;
                    }
                </style>
            </head>
            <body>
                ${chatWindowHTML}
                <script>
                    document.getElementById('separate-chat').style.display = 'none';
                    const chatWindow = document.getElementById('chat-window');
                    chatWindow.classList.add('separated');
                    const returnButton = document.getElementById('return-btn');
                    returnButton.style.display = 'inline-block'; // 분리된 창에서만 보이도록 설정
                    returnButton.onclick = function() {
                        const openerChatWindow = window.opener.document.getElementById('chat-window');
                        openerChatWindow.outerHTML = document.getElementById('chat-window').outerHTML;
                        window.opener.document.getElementById('chat-window').classList.remove('separated');
                        window.opener.document.getElementById('chat-window').style.display = 'flex';
                        window.opener.document.getElementById('main').style.marginRight = '30%';
                        window.close();
                    };
                <\/script>
            </body>
            </html>
        `);
        chatWindow.style.display = 'none';
        mainContent.style.marginRight = '0';
    };
});
