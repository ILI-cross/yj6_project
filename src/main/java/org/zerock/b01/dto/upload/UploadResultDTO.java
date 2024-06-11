package org.zerock.b01.dto.upload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadResultDTO {
    private String uuid;
    private String fileName;
    //이미지냐 이면 썸네일 만들거임
    private boolean img;
    public String getLink(){
        if(img){
            return "s+"+uuid+"_"+fileName;
        }else{
            return uuid+"_"+fileName;
        }
    }
}
