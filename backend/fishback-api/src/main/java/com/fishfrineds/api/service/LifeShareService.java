package com.fishfrineds.api.service;

import com.fishfrineds.api.dto.LifeShareDTO;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.LifeShare;
import com.fishfrineds.api.repository.LifeShareRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LifeShareService {
    
    private final LifeShareRepository lifeShareRepository;
    
    @Transactional
    public LifeShareDTO create(LifeShareDTO dto, Long userId) {
        LifeShare share = new LifeShare();
        BeanUtils.copyProperties(dto, share);
        share.setCreatedBy(userId);
        share.setIsDeleted(false);
        share.setViewCount(0);
        share.setLikeCount(0);
        
        LifeShare saved = lifeShareRepository.save(share);
        LifeShareDTO result = new LifeShareDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }
    
    @Transactional(readOnly = true)
    public PageResult<LifeShareDTO> findAll(Pageable pageable) {
        Page<LifeShare> page = lifeShareRepository.findByIsDeletedFalse(pageable);
        return PageResult.from(page.map(this::toDTO));
    }
    
    @Transactional(readOnly = true)
    public Optional<LifeShareDTO> findById(Long id) {
        return lifeShareRepository.findByIdAndIsDeletedFalse(id)
                .map(this::toDTO);
    }
    
    @Transactional
    public Optional<LifeShareDTO> update(Long id, LifeShareDTO dto, Long userId, boolean isAdmin) {
        return lifeShareRepository.findByIdAndIsDeletedFalse(id)
                .filter(share -> isAdmin || share.getCreatedBy().equals(userId))
                .map(share -> {
                    BeanUtils.copyProperties(dto, share, "id", "createdBy", "createdAt", "viewCount", "likeCount", "isDeleted");
                    LifeShare updated = lifeShareRepository.save(share);
                    return toDTO(updated);
                });
    }
    
    @Transactional
    public boolean delete(Long id, Long userId, boolean isAdmin) {
        return lifeShareRepository.findByIdAndIsDeletedFalse(id)
                .filter(share -> isAdmin || share.getCreatedBy().equals(userId))
                .map(share -> {
                    share.setIsDeleted(true);
                    lifeShareRepository.save(share);
                    return true;
                })
                .orElse(false);
    }
    
    @Transactional
    public boolean incrementViewCount(Long id) {
        return lifeShareRepository.findByIdAndIsDeletedFalse(id)
                .map(share -> {
                    share.setViewCount(share.getViewCount() + 1);
                    lifeShareRepository.save(share);
                    return true;
                })
                .orElse(false);
    }
    
    private LifeShareDTO toDTO(LifeShare share) {
        LifeShareDTO dto = new LifeShareDTO();
        BeanUtils.copyProperties(share, dto);
        return dto;
    }
}
