package org.zerock.jsontest.controller.board;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.jsontest.dto.board.LikeUserDTO;
import org.zerock.jsontest.service.board.LikeService;
import org.zerock.jsontest.service.board.LikeUserService;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final LikeUserService likeUserService;

    //게시글을 열면 좋아요 카운트도 함께 출력(비동기)
    // 게시글을 열면 좋아요 카운트와 좋아요 상태를 함께 반환
    @GetMapping(value = "/like/{bno}")
    public ResponseEntity<Map<String, Object>> getCount(@PathVariable("bno") Long bno, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String loginSession = (String) session.getAttribute("loginSession");

        Long likeCount = likeService.getLikeCount(bno);
        response.put("likeCount", likeCount);

        if (loginSession != null) {
            LikeUserDTO likeUserDTO = LikeUserDTO.builder()
                    .bno(bno)
                    .id(loginSession)
                    .build();
            boolean likeUserAlready = likeUserService.searchLikeUser(likeUserDTO);
            response.put("likeStatus", likeUserAlready);
        } else {
            response.put("likeStatus", false);
        }

        return ResponseEntity.ok(response);
    }

    //좋아요 버튼 클릭시 +1 카운트 , 값을 DB에 저장
    @PostMapping(value = "/like/{bno}")
    public ResponseEntity<Map<String, Object>> incrementLikeCount(@PathVariable("bno") Long bno, HttpSession session) {
        try {
            //좋아요 버튼 누른 유저 체크'

            String loginSession = (String) session.getAttribute("loginSession");
            System.out.println("로그인 세션: " + loginSession);
            LikeUserDTO likeUserDTO = LikeUserDTO.builder()
                    .bno(bno)
                    .id(loginSession)
                    .build();
            System.out.println("LikeUserDTO 생성: " + likeUserDTO);
            likeUserService.registerUserLike(likeUserDTO);
            System.out.println("LikeUser 저장 완료");


            Long newLikeCount = likeService.incrementLikeCount(bno);
            System.out.println("좋아요 카운트 증가: " + newLikeCount);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "좋아욘!");
            response.put("likeCount", newLikeCount);
            response.put("likeStatus", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to increment like count"));
        }
    }

    //좋아요 버튼이 이미 눌렸다면 한번 더 눌렀을때 -1 카운트 값을 DB에 저장
    @PostMapping(value = "/dislike/{bno}")
    public ResponseEntity<Map<String, Object>> decrementLikeCount(@PathVariable("bno") Long bno, HttpSession session) {
        try {

            //좋아요 버튼 누른 유저 체크해제
            String loginSession = (String) session.getAttribute("loginSession");
            System.out.println("로그인 세션: " + loginSession);
            LikeUserDTO likeUserDTO = LikeUserDTO.builder()
                    .bno(bno)
                    .id(loginSession)
                    .build();
            System.out.println("LikeUserDTO 삭제생성: " + likeUserDTO);

            likeUserService.deleteUserLike(likeUserDTO);
            System.out.println("LikeUser 삭제 완료");

            Long newLikeCount = likeService.decrementLikeCount(bno);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "좋아욘취소!");
            response.put("likeCount", newLikeCount);
            response.put("likeStatus", false);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to increment like count"));
        }
    }


}
