package com.fishfrineds.api.service;

import com.fishfrineds.api.dto.AnglerProfileDTO;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.AnglerProfile;
import com.fishfrineds.api.repository.AnglerProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnglerProfileService {
    
    private final AnglerProfileRepository anglerProfileRepository;
    
    @Transactional
    public AnglerProfileDTO create(AnglerProfileDTO dto, Long userId) {
        AnglerProfile profile = new AnglerProfile();
        BeanUtils.copyProperties(dto, profile);
        profile.setUserId(userId);
        
        AnglerProfile saved = anglerProfileRepository.save(profile);
        AnglerProfileDTO result = new AnglerProfileDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }
    
    @Transactional(readOnly = true)
    public PageResult<AnglerProfileDTO> findAll(Pageable pageable) {
        Page<AnglerProfile> page = anglerProfileRepository.findAll(pageable);
        return PageResult.from(page.map(this::toDTO));
    }
    
    @Transactional(readOnly = true)
    public Optional<AnglerProfileDTO> findById(Long id) {
        return anglerProfileRepository.findById(id)
                .map(this::toDTO);
    }
    
    @Transactional
    public Optional<AnglerProfileDTO> update(Long id, AnglerProfileDTO dto, Long userId, boolean isAdmin) {
        return anglerProfileRepository.findById(id)
                .filter(profile -> isAdmin || profile.getUserId().equals(userId))
                .map(profile -> {
                    BeanUtils.copyProperties(dto, profile, "id", "userId", "createdAt");
                    AnglerProfile updated = anglerProfileRepository.save(profile);
                    return toDTO(updated);
                });
    }
    
    @Transactional
    public boolean delete(Long id, Long userId, boolean isAdmin) {
        return anglerProfileRepository.findById(id)
                .filter(profile -> isAdmin || profile.getUserId().equals(userId))
                .map(profile -> {
                    anglerProfileRepository.delete(profile);
                    return true;
                })
                .orElse(false);
    }
    
    @Transactional(readOnly = true)
    public PageResult<AnglerProfileDTO> searchByName(String name, Pageable pageable) {
        Page<AnglerProfile> page = anglerProfileRepository.findByNameContaining(name, pageable);
        return PageResult.from(page.map(this::toDTO));
    }
    
    private AnglerProfileDTO toDTO(AnglerProfile profile) {
        AnglerProfileDTO dto = new AnglerProfileDTO();
        BeanUtils.copyProperties(profile, dto);
        return dto;
    }
}
