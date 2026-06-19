package com.fishfrineds.api.repository;

import com.fishfrineds.api.model.AnglerProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnglerProfileRepository extends JpaRepository<AnglerProfile, Long> {
    
    Page<AnglerProfile> findAll(Pageable pageable);
    
    Optional<AnglerProfile> findById(Long id);
    
    Page<AnglerProfile> findByNameContaining(String name, Pageable pageable);
    
    Page<AnglerProfile> findBySpecialityContaining(String speciality, Pageable pageable);
    
    long count();
}
