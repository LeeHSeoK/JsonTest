package org.zerock.jsontest.controller.board;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.jsontest.dto.board.upload.UploadFileDTO;
import org.zerock.jsontest.dto.board.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {
    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO) {
        if (uploadFileDTO.getFiles() != null) {
            final List<UploadResultDTO> list = new ArrayList<>();

            uploadFileDTO.getFiles().forEach(multipartFile -> {
                String originalName = multipartFile.getOriginalFilename();  // 업로드하려는 파일의 이름을 가져옵니다.
                log.info(originalName);
                String uuid = UUID.randomUUID().toString(); //uuid를 랜덤한 값으로 만들어서 파일이름뒤에 붙인다.
                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);
                boolean image = false;
                try {
                    multipartFile.transferTo(savePath); //파일을 지정한 경로에 저장하였다.
                    if (Files.probeContentType(savePath).startsWith("image")) { //저장한 파일이 이미지이면
                        image = true;
                        File thumbFile = new File(uploadPath, "s_" + uuid + "_" + originalName);    //썸네일 파일을 새롭게 만듭니다.
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100, 100);  //썸네일을 만들때 사이즈
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                                .uuid(uuid)
                                .fileName(originalName)
                                .img(image)
                                .build());  //몇가지의 파일을 UploadResultDTO 형식에 맡게 List에 저장한다.
            });
            return list;
        }
        return null;
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName) {
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName); // seperator는 / \를 알아서 확인해준다. 파일경로에 있는 파일을 resource에서 관리
//        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();    // 정보들을 header에 담아서 전송하려한다.

        try{
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        }catch(IOException e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource); //해더에 해더값들을 넣고 바디에 파일을 담는다.
    }

    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> remove(@PathVariable String fileName) {
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName); //해당파일을 FileSystemResource을 통해 찾아서 resource 변수에 담는다.
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try{
            String contentType = Files.probeContentType(resource.getFile().toPath());   //해당 파일의 컨탠츠 타입을 변수에 contentType변수에 담는다.
            removed = resource.getFile().delete();  // 해당파일을 삭제하고 삭제되면 removed값 true

            if(contentType.startsWith("image")) {   //해당 파일이 이미지였는지 확인한다.
                File thumbFile = new File(uploadPath+File.separator+"s_"+fileName); //이미지파일이면 해당 파일이 생성될때 같이만들어졌던 썸네일파일도 같이삭제
                thumbFile.delete();
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

        resultMap.put("removed", removed);
        return resultMap;
    }
}
