package com.fishfrineds.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnglerProfileDTO {
    
    private Long id;
    
    @NotBlank(message = "姓名不能为空")
    @Size(max = 100, message = "姓名最多100字符")
    private String name;
    
    @Size(max = 2000, message = "简介最多2000字符")
    private String bio;
    
    private Integer experienceYears;
    
    @Size(max = 200, message = "专长最多200字符")
    private String speciality;
    
    @Size(max = 200, message = "联系方式最多200字符")
    private String contact;
    
    @Size(max = 500, message = "图片URL最多500字符")
    private String imageUrl;
    
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
