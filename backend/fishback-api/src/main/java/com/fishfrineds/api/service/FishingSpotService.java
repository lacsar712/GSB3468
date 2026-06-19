package com.fishfrineds.api.service;

import com.fishfrineds.api.dto.FishingSpotDTO;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.FishingSpot;
import com.fishfrineds.api.repository.FishingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FishingSpotService {
    
    private final FishingSpotRepository fishingSpotRepository;
    
    @Transactional
    public FishingSpotDTO create(FishingSpotDTO dto, Long userId) {
        FishingSpot spot = new FishingSpot();
        BeanUtils.copyProperties(dto, spot);
        spot.setCreatedBy(userId);
        spot.setIsDeleted(false);
        
        FishingSpot saved = fishingSpotRepository.save(spot);
        FishingSpotDTO result = new FishingSpotDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }
    
    @Transactional(readOnly = true)
    public PageResult<FishingSpotDTO> findAll(Pageable pageable) {
        Page<FishingSpot> page = fishingSpotRepository.findByIsDeletedFalse(pageable);
        return PageResult.from(page.map(this::toDTO));
    }
    
    @Transactional(readOnly = true)
    public Optional<FishingSpotDTO> findById(Long id) {
        return fishingSpotRepository.findByIdAndIsDeletedFalse(id)
                .map(this::toDTO);
    }
    
    @Transactional
    public Optional<FishingSpotDTO> update(Long id, FishingSpotDTO dto, Long userId, boolean isAdmin) {
        return fishingSpotRepository.findByIdAndIsDeletedFalse(id)
                .filter(spot -> isAdmin || spot.getCreatedBy().equals(userId))
                .map(spot -> {
                    BeanUtils.copyProperties(dto, spot, "id", "createdBy", "createdAt", "isDeleted");
                    FishingSpot updated = fishingSpotRepository.save(spot);
                    return toDTO(updated);
                });
    }
    
    @Transactional
    public boolean delete(Long id, Long userId, boolean isAdmin) {
        return fishingSpotRepository.findByIdAndIsDeletedFalse(id)
                .filter(spot -> isAdmin || spot.getCreatedBy().equals(userId))
                .map(spot -> {
                    spot.setIsDeleted(true);
                    fishingSpotRepository.save(spot);
                    return true;
                })
                .orElse(false);
    }
    
    @Transactional(readOnly = true)
    public PageResult<FishingSpotDTO> searchByTitle(String title, Pageable pageable) {
        Page<FishingSpot> page = fishingSpotRepository.findByTitleContainingAndIsDeletedFalse(title, pageable);
        return PageResult.from(page.map(this::toDTO));
    }
    
    private FishingSpotDTO toDTO(FishingSpot spot) {
        FishingSpotDTO dto = new FishingSpotDTO();
        BeanUtils.copyProperties(spot, dto);
        return dto;
    }
}
