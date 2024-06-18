package org.zerock.jsontest.controller.board;

import jakarta.servlet.http.HttpSession;
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

    @GetMapping(value = "/like/{bno}")
    public Long getCount(@PathVariable("bno") Long bno) {
        return likeService.getLikeCount(bno);
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
            Map<String, Object> response = new HashMap<>();
            response.put("message", "좋아욘!");
            response.put("likeCount", newLikeCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to increment like count"));
        }
    }

    @PostMapping(value = "/dislike/{bno}")
    public ResponseEntity<Map<String, Object>> decrementLikeCount(@PathVariable("bno") Long bno) {
        try {


            Long newLikeCount = likeService.decrementLikeCount(bno);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "좋아욘취소!");
            response.put("likeCount", newLikeCount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to increment like count"));
        }
    }


}
