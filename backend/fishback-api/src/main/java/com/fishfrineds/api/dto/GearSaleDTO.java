package com.fishfrineds.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GearSaleDTO {
    
    private Long id;
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最多200字符")
    private String title;
    
    @Size(max = 5000, message = "描述最多5000字符")
    private String description;
    
    @NotNull(message = "价格不能为空")
    @Positive(message = "价格必须为正数")
    private BigDecimal price;
    
    @Size(max = 100, message = "分类最多100字符")
    private String category;
    
    @Size(max = 50, message = "状态最多50字符")
    private String conditionStatus;
    
    @Size(max = 200, message = "联系方式最多200字符")
    private String contact;
    
    @Size(max = 500, message = "图片URL最多500字符")
    private String imageUrl;
    
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
