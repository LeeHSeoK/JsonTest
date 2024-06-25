

// // 이미지 개수 확인 함수
// function checkImageCount() {
//     var images = document.querySelectorAll('.image');
// }
//
// // 페이지 로드 시 이미지 개수 확인
// window.onload = function () {
//     checkImageCount();
// };

// 버튼 클릭 시 이미지 업데이트 함수
// click시 키워드로 검색하는 jsp
function updateImages(keyword) {
    $.ajax({
        type: "GET",
        url: `https://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey=tussmhxuu1xAlRv9cJ9NQP8LtOwmZjojrJeUaGLT6sI0jd91wXKCsuR4Yak8I08mDILJw17m2HRvvjXJNYzNnQ%3D%3D&numOfRows=10&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&keyword=${keyword}&contentTypeId=12`,
        data: {},
        success: function(response) {
            let items = response.response.body.items.item;
            console.log(items);
            console.log("들어오는거 확인")

            const img = [];
            for (let i = 0; i < items.length; i++) {
                if (items[i].firstimage && items[i].firstimage.trim() !== "") {
                    img.push(`<a href="/searchresultinfo?contentId=${items[i].contentid}&contentTypeId=${items[i].contentTypeId}"><img src="${items[i].firstimage}" alt="Image ${i + 1}" class="box"></a>`);
                }
                if (img.length >= 4) {
                    break;
                }
            }

            // 이미지를 화면에 출력
            $('#wrap').empty().append(img.join(''));

            // 이미지 태그에 src 속성 설정
            for (let i = 0; i < img.length; i++) {
                let imgElement = $(img[i]).find('img');
                $(`#image${i + 1}`).attr('src', imgElement.attr('src'));
            }
        },
        error: function(xhr, status, error) {
            console.error('Error fetching data:', error);
        }
    });
}

//
// // 페이지 로드 시 슬라이드 쇼 관련 코드
// document.addEventListener("DOMContentLoaded", function() {
//     let slideIndex = 0;
//     let slides = document.querySelectorAll(".slide");
//     let slideContainer = document.querySelector(".slides");
//     let pauseButton = document.getElementById("pauseButton");
//     let isPaused = false;
//     let slideInterval;
//
//     function showSlides() {
//         if (!isPaused) {
//             slideIndex++;
//             if (slideIndex >= slides.length) {
//                 slideIndex = 0;
//             }
//             slideContainer.style.transform = `translateX(-${slideIndex * 100}%)`;
//             slideInterval = setTimeout(showSlides, 3000); // 3초마다 이미지 변경
//         }
//     }
//
//     function plusSlides(n) {
//         clearTimeout(slideInterval);
//         slideIndex += n;
//         if (slideIndex >= slides.length) {
//             slideIndex = 0;
//         }
//         if (slideIndex < 0) {
//             slideIndex = slides.length - 1;
//         }
//         slideContainer.style.transform = `translateX(-${slideIndex * 100}%)`;
//         if (!isPaused) {
//             slideInterval = setTimeout(showSlides, 3000);
//         }
//     }
//
//     function togglePause() {
//         isPaused = !isPaused;
//         pauseButton.textContent = isPaused ? "Play" : "Pause";
//         if (!isPaused) {
//             showSlides();
//         } else {
//             clearTimeout(slideInterval);
//         }
//     }
//
//     showSlides();
//
//     // 버튼 슬라이드 기능
//     const buttonRow = document.querySelector('.button-row');
//     let isDown = false;
//     let startX;
//     let scrollLeft;
//
//     buttonRow.addEventListener('mousedown', (e) => {
//         isDown = true;
//         buttonRow.classList.add('active');
//         startX = e.pageX - buttonRow.offsetLeft;
//         scrollLeft = buttonRow.scrollLeft;
//     });
//
//     buttonRow.addEventListener('mouseleave', () => {
//         isDown = false;
//         buttonRow.classList.remove('active');
//     });
//
//     buttonRow.addEventListener('mouseup', () => {
//         isDown = false;
//         buttonRow.classList.remove('active');
//     });
//
//     buttonRow.addEventListener('mousemove', (e) => {
//         if (!isDown) return;
//         e.preventDefault();
//         const x = e.pageX - buttonRow.offsetLeft;
//         const walk = (x - startX) * 2; // 스크롤 속도 증가
//         buttonRow.scrollLeft = scrollLeft - walk;
//     });
//
//     // AI Chat 관련 코드
//     const aichatBtn = document.getElementById('aichat-btn');
//     const chatWindow = document.getElementById('chat-window');
//     const mainContent = document.getElementById('main');
//
//     aichatBtn.addEventListener('click', toggleChat);
//
//     function toggleChat() {
//         if (chatWindow.style.display === 'none' || chatWindow.style.display === '') {
//             chatWindow.style.display = 'flex';
//             mainContent.style.marginRight = '30%'; // 오른쪽 여백 추가
//         } else {
//             chatWindow.style.display = 'none';
//             mainContent.style.marginRight = '0'; // 오른쪽 여백 제거
//         }
//     }
//
//     window.separateChat = function() {
//         const chatWindowHTML = chatWindow.outerHTML;
//         const newWindow = window.open('', '', 'width=400,height=600');
//         newWindow.document.write(`
//             <!DOCTYPE html>
//             <html lang="en">
//             <head>
//                 <meta charset="UTF-8">
//                 <meta name="viewport" content="width=device-width, initial-scale=1.0">
//                 <title>Separated Chat</title>
//                 <link rel="stylesheet" href="chat.css">
//                 <style>
//                     body {
//                         margin: 0;
//                         padding: 0;
//                         display: flex;
//                         justify-content: center;
//                         align-items: center;
//                         height: 100vh;
//                         overflow: hidden;
//                     }
//                     .chat-window {
//                         width: 100%;
//                         height: 100%;
//                     }
//                 </style>
//             </head>
//             <body>
//                 ${chatWindowHTML}
//                 <script>
//                     document.getElementById('separate-chat').style.display = 'none';
//                     const chatWindow = document.getElementById('chat-window');
//                     chatWindow.classList.add('separated');
//                     const returnButton = document.getElementById('return-btn');
//                     returnButton.style.display = 'inline-block'; // 분리된 창에서만 보이도록 설정
//                     returnButton.onclick = function() {
//                         const openerChatWindow = window.opener.document.getElementById('chat-window');
//                         openerChatWindow.outerHTML = document.getElementById('chat-window').outerHTML;
//                         window.opener.document.getElementById('chat-window').classList.remove('separated');
//                         window.opener.document.getElementById('chat-window').style.display = 'flex';
//                         window.opener.document.getElementById('main').style.marginRight = '30%';
//                         window.close();
//                     };
//                 <\/script>
//             </body>
//             </html>
//         `);
//         chatWindow.style.display = 'none';
//         mainContent.style.marginRight = '0';
//     };
// });
// });