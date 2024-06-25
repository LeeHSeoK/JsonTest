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

    //좋아요수를 출력하는 컨트롤러 url에서 게시판의 bno값을 활용한다.
    @GetMapping(value = "/like/{bno}")
    public ResponseEntity<Map<String, Object>> getCount(@PathVariable("bno") Long bno, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        String loginSession = (String) session.getAttribute("loginSession");    //새션에 담긴 아이디값을 가져온다.

        Long likeCount = likeService.getLikeCount(bno); //해당 bno를 likeservice에 있는 getlikecount메서드를 통해 좋아요수를 가져온다.
        response.put("likeCount", likeCount);//가져온 좋아요수를 response에 담는다.

        if (loginSession != null) { //로그인이 되어있다면 LikeUserDTO에 ID와 bno값을 담아 LikeUserSerivce에 있는 searchLikeUser메서드를 통해 해당 ID가 이전에 게시글에 좋아요를 눌렀는지 확인한다.
            LikeUserDTO likeUserDTO = LikeUserDTO.builder()
                    .bno(bno)
                    .id(loginSession)
                    .build();
            boolean likeUserAlready = likeUserService.searchLikeUser(likeUserDTO);
            response.put("likeStatus", likeUserAlready);    //좋아요를 누른적이 있다면 해당 값(true)을 response에 담는다.
        } else {
            response.put("likeStatus", false); //없다면 false값을 담는다
        }

        return ResponseEntity.ok(response);
    }

    //좋아요수를 증가시키고 해당 값을 DB에 저장하는 컨트롤러입니다.
    @PostMapping(value = "/like/{bno}")//url에서 게시판의 bno값을 활용한다.
    public ResponseEntity<Map<String, Object>> incrementLikeCount(@PathVariable("bno") Long bno, @RequestBody Map<String, String> requestBody, HttpSession session) {
        try {
            String loginSession = (String) session.getAttribute("loginSession"); //새션에 담긴 아이디값을 가져온다.
            String placeName = requestBody.get("placeName");
            LikeUserDTO likeUserDTO = LikeUserDTO.builder() //로그인이 되어있다면 LikeUserDTO에 ID와 bno값을 담아 LikeUserSerivce에 있는 registerUserLike메서드를 통해 해당 유저가 좋아요 클릭을 했다는 것을 알수있는 데이터를 DB에 저장한다.
                    .bno(bno)
                    .id(loginSession)
                    .placeName(placeName)
                    .build();
            likeUserService.registerUserLike(likeUserDTO);

            Long newLikeCount = likeService.incrementLikeCount(bno); //bno를 이용해 LikeUserService에 있는 incrementLikeCount메서드를 통해 총 좋아요수를 1증가 시키고 DB에 저장한다.
            System.out.println(newLikeCount+"incre=========================================");

            Map<String, Object> response = new HashMap<>(); //해쉬맵을 하나 만들어서 값들을 담는다
            response.put("message", "좋아요!");//알람메세지로 사용될 message값
            response.put("likeCount", newLikeCount);//총 좋아요수 업데이트를 위한 likeCount값
            response.put("likeStatus", true);//좋아요를 눌렀던 적이있는지 확인하는 상태값인 likeStatus
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Failed to increment like count"));
        }
    }

    //좋아요를 취소하고 총 좋아요수를 1감소시키고 해당값들을 DB에 저장하는 컨트롤러입니다. (증가하는 컨트롤러와 동일합니다.)
    @PostMapping(value = "/dislike/{bno}")
    public ResponseEntity<Map<String, Object>> decrementLikeCount(@PathVariable("bno") Long bno, @RequestBody Map<String, String> requestBody, HttpSession session) {
        try {

            String placeName = requestBody.get("placeName");
            String loginSession = (String) session.getAttribute("loginSession");
            LikeUserDTO likeUserDTO = LikeUserDTO.builder()
                    .bno(bno)
                    .id(loginSession)
                    .placeName(placeName)
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
