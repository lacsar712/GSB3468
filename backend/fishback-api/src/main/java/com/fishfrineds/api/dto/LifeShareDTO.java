package com.fishfrineds.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LifeShareDTO {
    
    private Long id;
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最多200字符")
    private String title;
    
    @Size(max = 50, message = "分享类型最多50字符")
    private String shareType;
    
    @Size(max = 500, message = "图片URL最多500字符")
    private String imageUrl;
    
    @Size(max = 5000, message = "内容最多5000字符")
    private String content;
    
    private Integer viewCount;
    private Integer likeCount;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
