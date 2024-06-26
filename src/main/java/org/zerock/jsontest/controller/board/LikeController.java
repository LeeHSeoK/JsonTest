package org.zerock.jsontest.controller.board;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/like/{bno}")
    public ResponseEntity<Map<String, Object>> incrementLikeCount(@PathVariable("bno") Long bno, HttpSession session) {
        try {
            String loginSession = (String) session.getAttribute("loginSession");
            LikeUserDTO likeUserDTO = LikeUserDTO.builder()
                    .bno(bno)
                    .id(loginSession)
                    .build();
            likeUserService.registerUserLike(likeUserDTO);

            Long newLikeCount = likeService.incrementLikeCount(bno);
            System.out.println(newLikeCount+"incre=========================================");

            Map<String, Object> response = new HashMap<>();
            response.put("message", "좋아요!");
            response.put("likeCount", newLikeCount);
            response.put("likeStatus", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to increment like count"));
        }
    }

    @PostMapping(value = "/dislike/{bno}")
    public ResponseEntity<Map<String, Object>> decrementLikeCount(@PathVariable("bno") Long bno, HttpSession session) {
        try {
            String loginSession = (String) session.getAttribute("loginSession");
            LikeUserDTO likeUserDTO = LikeUserDTO.builder()
                    .bno(bno)
                    .id(loginSession)
                    .build();
            likeUserService.deleteUserLike(likeUserDTO);

            Long newLikeCount = likeService.decrementLikeCount(bno);
            System.out.println(newLikeCount+"decre=========================================");

            Map<String, Object> response = new HashMap<>();
            response.put("message", "좋아요 취소!");
            response.put("likeCount", newLikeCount);
            response.put("likeStatus", false);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to decrement like count"));
        }
    }

}
