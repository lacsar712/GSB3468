package com.fishfrineds.api.repository;

import com.fishfrineds.api.model.GearSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GearSaleRepository extends JpaRepository<GearSale, Long> {
    
    Page<GearSale> findAll(Pageable pageable);
    
    Optional<GearSale> findById(Long id);
    
    Page<GearSale> findByCategory(String category, Pageable pageable);
    
    Page<GearSale> findByTitleContaining(String title, Pageable pageable);
    
    long count();
}
