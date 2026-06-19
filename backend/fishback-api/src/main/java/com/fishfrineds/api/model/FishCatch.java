package com.fishfrineds.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "fish_catches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishCatch {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(name = "fish_type", nullable = false, length = 100)
    private String fishType;
    
    @Column(name = "fish_weight", precision = 8, scale = 2)
    private BigDecimal fishWeight;
    
    @Column(name = "fish_length", precision = 8, scale = 2)
    private BigDecimal fishLength;
    
    @Column(name = "fishing_spot", length = 200)
    private String fishingSpot;
    
    @Column(name = "fishing_method", length = 100)
    private String fishingMethod;
    
    @Column(name = "bait_used", length = 200)
    private String baitUsed;
    
    @Column(name = "weather", length = 100)
    private String weather;
    
    @Column(name = "catch_date")
    private LocalDateTime catchDate;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "created_by", nullable = false)
    private Long createdBy;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
