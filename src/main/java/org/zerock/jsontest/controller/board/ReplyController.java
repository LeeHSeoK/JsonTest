package org.zerock.jsontest.controller.board;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.zerock.jsontest.dto.board.PageRequestDTO;
import org.zerock.jsontest.dto.board.PageResponseDTO;
import org.zerock.jsontest.dto.board.ReplyDTO;
import org.zerock.jsontest.service.board.ReplyService;

import java.util.HashMap;
import java.util.Map;

//들어온 JSON값이 @RequsetMapping을 통해서 해당경로로 들어오는데 사용하기위해선 replyDTO모양으로 만들어져야하고
//해당 내용을 변환해 주는게 @RequestBody이다.
@RestController
@RequestMapping("/replies")
@Log4j2
@RequiredArgsConstructor
public class ReplyController {
//consumes = MediaType.APPLICATION_JSON_VALUE JSON형식을 받겠다.
//ResponseEntity는 map클래스로 만들어져있는 값을 Json형식으로 바꿔준다.
    private final ReplyService replyService;//의존성 주입

    @PostMapping(value="/",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                                        BindingResult bindingResult) throws BindException {
        log.info(replyDTO.toString());
        if(bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Map<String, Long> resultMap = new HashMap<>();
        Long rno = replyService.register(replyDTO);

        resultMap.put("rno",rno);

        return resultMap;
    }

    @GetMapping(value="/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard(bno, pageRequestDTO);
        return responseDTO;

    }

    @GetMapping(value="/{rno}")
    public ReplyDTO getReplyDTO(@PathVariable("rno") Long rno) {
        ReplyDTO replyDTO = replyService.readOne(rno);
        return replyDTO;
    }

    @DeleteMapping("/{rno}")
    public Map<String, Long> remove(@PathVariable("rno") Long rno) {
        replyService.remove(rno);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);
        return resultMap;
    }

    @PutMapping(value="/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> modify (@PathVariable("rno") Long rno, @RequestBody ReplyDTO replyDTO) {

        replyService.modify(replyDTO);
        Map<String, Long> resultMap = new HashMap<>();
        resultMap.put("rno",rno);
        return resultMap;

    }
}
