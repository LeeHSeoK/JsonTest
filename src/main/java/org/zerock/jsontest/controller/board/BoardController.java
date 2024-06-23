package org.zerock.jsontest.controller.board;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.jsontest.dto.board.*;
import org.zerock.jsontest.service.board.BoardService;
import org.zerock.jsontest.service.board.LoginService;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    private final BoardService boardService;
    private final LoginService loginService;

    @GetMapping("list")
    public void list(PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    @GetMapping("/register")
    public void registerGET(HttpSession session, Model model){
        String loginSession = (String) session.getAttribute("loginSession");
        SignUpDTO signUpDTO = loginService.searchOne(loginSession);
        model.addAttribute("userInfo", signUpDTO);
    }

    @PostMapping("/register")
    public String registerPOST(@Valid BoardDTO boardDTO, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/register";
        }

        System.out.println(boardDTO.toString()+"==============================boardDTO");
        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);
        return "redirect:/board/list";
    }

    @GetMapping({"/read", "/modify"})
    public void read(@RequestParam("bno") Long bno, PageRequestDTO pageRequestDTO, Model model, HttpSession session){
        boardService.incrementViewCount(bno); // 조회수 증가
        BoardDTO boardDTO = boardService.readOne(bno);
        model.addAttribute("dto", boardDTO);
        String loginSession = (String) session.getAttribute("loginSession");

        if (loginSession != null) {
            SignUpDTO signUpDTO = loginService.searchOne(loginSession);
            model.addAttribute("userName", signUpDTO.getName());
        } else {
            model.addAttribute("userName", "");
        }
    }

    @PostMapping("/modify")
    public String modify(PageRequestDTO pageRequestDTO,
                         @Valid BoardDTO boardDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors",
                    bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?" + link;
        }
        System.out.println(boardDTO + "=====================================");
        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "modified");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());
        return "redirect:/board/read";
    }

    @PostMapping("/delete")
    public String delete(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        Long bno = boardDTO.getBno();
        boardService.remove(bno);

        // 게시물이 삭제되었고 첨부파일 삭제
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }
        redirectAttributes.addFlashAttribute("result", "deleted");
        return "redirect:/board/list";
    }

    public void removeFiles(List<String> fileNames){
        for(String fileName : fileNames){
            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);

            try{
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();
                if(contentType.startsWith("image")){
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }

}
