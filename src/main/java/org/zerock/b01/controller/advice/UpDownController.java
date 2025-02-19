package org.zerock.b01.controller.advice;

import groovy.util.logging.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.b01.dto.upload.UploadFileDTO;
import org.zerock.b01.dto.upload.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@lombok.extern.log4j.Log4j2
@RestController
@Log4j2
public class UpDownController {
    @Value("${org.zerock.upload.path}")
    private String uploadPath;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){
        if(uploadFileDTO.getFiles() !=null ){
            final List<UploadResultDTO> list = new ArrayList<>();
            uploadFileDTO.getFiles().forEach(multipartFile ->{
                String originalName = multipartFile.getOriginalFilename();
                log.info(originalName);
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);
                boolean image = false;
                try{
                    multipartFile.transferTo(savePath);
                    if(Files.probeContentType(savePath).startsWith("image")){
                        image = true;
                        File thumbFile = new File(uploadPath, "s_"+uuid+"_"+originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 100, 100);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }

                list.add(UploadResultDTO.builder()
                                .uuid(uuid)
                                .fileName(originalName)
                                .img(image).build());
            });
            return list;
        }
        return null;
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> view(@PathVariable String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));

        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);

    }

    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName){
        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;

        try{
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();

            if(contentType.startsWith("image")){
                File thumbFile = new File(uploadPath+File.separator+"s_"+fileName);
                thumbFile.delete();
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);
        return resultMap;

    }

}
