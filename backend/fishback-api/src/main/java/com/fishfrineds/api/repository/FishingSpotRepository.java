package com.fishfrineds.api.repository;

import com.fishfrineds.api.model.FishingSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FishingSpotRepository extends JpaRepository<FishingSpot, Long> {
    
    Page<FishingSpot> findByIsDeletedFalse(Pageable pageable);
    
    Optional<FishingSpot> findByIdAndIsDeletedFalse(Long id);
    
    Page<FishingSpot> findByTitleContainingAndIsDeletedFalse(String title, Pageable pageable);
    
    Page<FishingSpot> findByLocationContainingAndIsDeletedFalse(String location, Pageable pageable);
    
    long countByIsDeletedFalse();
}
