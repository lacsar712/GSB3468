package com.fishfrineds.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FishCatchDTO {

    private Long id;

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最多200字符")
    private String title;

    @NotBlank(message = "鱼种不能为空")
    @Size(max = 100, message = "鱼种最多100字符")
    private String fishType;

    private BigDecimal fishWeight;

    private BigDecimal fishLength;
    
    @Size(max = 200, message = "钓点最多200字符")
    private String fishingSpot;
    
    @Size(max = 100, message = "钓法最多100字符")
    private String fishingMethod;
    
    @Size(max = 200, message = "饵料最多200字符")
    private String baitUsed;
    
    @Size(max = 100, message = "天气最多100字符")
    private String weather;
    
    private LocalDateTime catchDate;
    
    @Size(max = 500, message = "图片URL最多500字符")
    private String imageUrl;
    
    @Size(max = 5000, message = "内容最多5000字符")
    private String content;
    
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
