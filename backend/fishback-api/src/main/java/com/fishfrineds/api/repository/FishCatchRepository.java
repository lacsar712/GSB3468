package com.fishfrineds.api.repository;

import com.fishfrineds.api.model.FishCatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FishCatchRepository extends JpaRepository<FishCatch, Long> {
    
    Page<FishCatch> findByIsDeletedFalse(Pageable pageable);
    
    Optional<FishCatch> findByIdAndIsDeletedFalse(Long id);
    
    Page<FishCatch> findByFishTypeContainingAndIsDeletedFalse(String fishType, Pageable pageable);
    
    Page<FishCatch> findByFishTypeAndIsDeletedFalse(String fishType, Pageable pageable);
    
    Page<FishCatch> findByCreatedByAndIsDeletedFalse(Long createdBy, Pageable pageable);
    
    long countByIsDeletedFalse();
    
    @Query("SELECT DISTINCT f.fishType FROM FishCatch f WHERE f.isDeleted = false AND f.fishType IS NOT NULL AND f.fishType <> '' ORDER BY f.fishType ASC")
    List<String> findDistinctFishTypes();
}
