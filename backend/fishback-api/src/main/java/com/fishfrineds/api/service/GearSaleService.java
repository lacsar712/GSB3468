package com.fishfrineds.api.service;

import com.fishfrineds.api.dto.GearSaleDTO;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.GearSale;
import com.fishfrineds.api.repository.GearSaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GearSaleService {
    
    private final GearSaleRepository gearSaleRepository;
    
    @Transactional
    public GearSaleDTO create(GearSaleDTO dto, Long userId) {
        GearSale gearSale = new GearSale();
        BeanUtils.copyProperties(dto, gearSale);
        gearSale.setUserId(userId);
        
        GearSale saved = gearSaleRepository.save(gearSale);
        GearSaleDTO result = new GearSaleDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }
    
    @Transactional(readOnly = true)
    public PageResult<GearSaleDTO> findAll(Pageable pageable) {
        Page<GearSale> page = gearSaleRepository.findAll(pageable);
        return PageResult.from(page.map(this::toDTO));
    }
    
    @Transactional(readOnly = true)
    public Optional<GearSaleDTO> findById(Long id) {
        return gearSaleRepository.findById(id)
                .map(this::toDTO);
    }
    
    @Transactional
    public Optional<GearSaleDTO> update(Long id, GearSaleDTO dto, Long userId, boolean isAdmin) {
        return gearSaleRepository.findById(id)
                .filter(gearSale -> isAdmin || gearSale.getUserId().equals(userId))
                .map(gearSale -> {
                    BeanUtils.copyProperties(dto, gearSale, "id", "userId", "createdAt");
                    GearSale updated = gearSaleRepository.save(gearSale);
                    return toDTO(updated);
                });
    }
    
    @Transactional
    public boolean delete(Long id, Long userId, boolean isAdmin) {
        return gearSaleRepository.findById(id)
                .filter(gearSale -> isAdmin || gearSale.getUserId().equals(userId))
                .map(gearSale -> {
                    gearSaleRepository.delete(gearSale);
                    return true;
                })
                .orElse(false);
    }
    
    @Transactional(readOnly = true)
    public PageResult<GearSaleDTO> findByCategory(String category, Pageable pageable) {
        Page<GearSale> page = gearSaleRepository.findByCategory(category, pageable);
        return PageResult.from(page.map(this::toDTO));
    }
    
    private GearSaleDTO toDTO(GearSale gearSale) {
        GearSaleDTO dto = new GearSaleDTO();
        BeanUtils.copyProperties(gearSale, dto);
        return dto;
    }
}
