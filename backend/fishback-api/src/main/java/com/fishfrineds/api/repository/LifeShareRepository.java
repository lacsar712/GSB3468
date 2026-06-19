package com.fishfrineds.api.repository;

import com.fishfrineds.api.model.LifeShare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LifeShareRepository extends JpaRepository<LifeShare, Long> {
    
    Page<LifeShare> findByIsDeletedFalse(Pageable pageable);
    
    Optional<LifeShare> findByIdAndIsDeletedFalse(Long id);
    
    Page<LifeShare> findByShareTypeAndIsDeletedFalse(String shareType, Pageable pageable);
    
    Page<LifeShare> findByCreatedByAndIsDeletedFalse(Long createdBy, Pageable pageable);
    
    long countByIsDeletedFalse();
}
