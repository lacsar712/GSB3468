package com.fishfrineds.api.service;

import com.fishfrineds.api.dto.FishCatchDTO;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.FishCatch;
import com.fishfrineds.api.repository.FishCatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FishCatchService {
    
    private final FishCatchRepository fishCatchRepository;
    
    @Transactional
    public FishCatchDTO create(FishCatchDTO dto, Long userId) {
        FishCatch catch_ = new FishCatch();
        BeanUtils.copyProperties(dto, catch_);
        catch_.setCreatedBy(userId);
        catch_.setIsDeleted(false);
        
        FishCatch saved = fishCatchRepository.save(catch_);
        FishCatchDTO result = new FishCatchDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }
    
    @Transactional(readOnly = true)
    public PageResult<FishCatchDTO> findAll(Pageable pageable) {
        Page<FishCatch> page = fishCatchRepository.findByIsDeletedFalse(pageable);
        return PageResult.from(page.map(this::toDTO));
    }
    
    @Transactional(readOnly = true)
    public Optional<FishCatchDTO> findById(Long id) {
        return fishCatchRepository.findByIdAndIsDeletedFalse(id)
                .map(this::toDTO);
    }
    
    @Transactional
    public Optional<FishCatchDTO> update(Long id, FishCatchDTO dto, Long userId, boolean isAdmin) {
        return fishCatchRepository.findByIdAndIsDeletedFalse(id)
                .filter(catch_ -> isAdmin || catch_.getCreatedBy().equals(userId))
                .map(catch_ -> {
                    BeanUtils.copyProperties(dto, catch_, "id", "createdBy", "createdAt", "isDeleted");
                    FishCatch updated = fishCatchRepository.save(catch_);
                    return toDTO(updated);
                });
    }
    
    @Transactional
    public boolean delete(Long id, Long userId, boolean isAdmin) {
        return fishCatchRepository.findByIdAndIsDeletedFalse(id)
                .filter(catch_ -> isAdmin || catch_.getCreatedBy().equals(userId))
                .map(catch_ -> {
                    catch_.setIsDeleted(true);
                    fishCatchRepository.save(catch_);
                    return true;
                })
                .orElse(false);
    }
    
    @Transactional(readOnly = true)
    public PageResult<FishCatchDTO> findByFishType(String fishType, Pageable pageable) {
        Page<FishCatch> page = fishCatchRepository.findByFishTypeAndIsDeletedFalse(fishType, pageable);
        return PageResult.from(page.map(this::toDTO));
    }
    
    @Transactional(readOnly = true)
    public List<String> findDistinctFishTypes() {
        return fishCatchRepository.findDistinctFishTypeByIsDeletedFalse();
    }
    
    private FishCatchDTO toDTO(FishCatch catch_) {
        FishCatchDTO dto = new FishCatchDTO();
        BeanUtils.copyProperties(catch_, dto);
        return dto;
    }
}
