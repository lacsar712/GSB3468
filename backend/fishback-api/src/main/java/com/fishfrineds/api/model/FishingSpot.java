package com.fishfrineds.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "fishing_spots")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FishingSpot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(nullable = false, length = 500)
    private String location;
    
    @Column(name = "location_desc", length = 1000)
    private String locationDesc;
    
    @Column(name = "fish_types", length = 500)
    private String fishTypes;
    
    @Column(name = "best_season", length = 100)
    private String bestSeason;
    
    @Column(name = "best_time", length = 100)
    private String bestTime;
    
    @Column(name = "facility_desc", length = 1000)
    private String facilityDesc;
    
    @Column(name = "fee_info", length = 500)
    private String feeInfo;
    
    @Column(name = "contact_info", length = 200)
    private String contactInfo;
    
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
