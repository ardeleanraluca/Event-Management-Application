package com.demo.project.dto;

import com.demo.project.entity.ImageEntity;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ImageDto{
    private String name;

    private String type;

    private byte[] picByte;

    public ImageDto(ImageEntity imageEntity){
       this.name = imageEntity.getName();
       this.type = imageEntity.getType();
       this.picByte = imageEntity.getPicByte();
    }
}
