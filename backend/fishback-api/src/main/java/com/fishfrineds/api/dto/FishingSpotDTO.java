package com.fishfrineds.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FishingSpotDTO {
    
    private Long id;
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最多200字符")
    private String title;
    
    @NotBlank(message = "位置不能为空")
    @Size(max = 500, message = "位置最多500字符")
    private String location;
    
    @Size(max = 1000, message = "位置描述最多1000字符")
    private String locationDesc;
    
    @Size(max = 500, message = "鱼类信息最多500字符")
    private String fishTypes;
    
    @Size(max = 100, message = "最佳季节最多100字符")
    private String bestSeason;
    
    @Size(max = 100, message = "最佳时间最多100字符")
    private String bestTime;
    
    @Size(max = 1000, message = "设施描述最多1000字符")
    private String facilityDesc;
    
    @Size(max = 500, message = "费用信息最多500字符")
    private String feeInfo;
    
    @Size(max = 200, message = "联系方式最多200字符")
    private String contactInfo;
    
    @Size(max = 500, message = "图片URL最多500字符")
    private String imageUrl;
    
    @Size(max = 5000, message = "内容最多5000字符")
    private String content;
    
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
