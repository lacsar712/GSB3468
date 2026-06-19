package com.fishfrineds.api.controller;

import com.fishfrineds.api.dto.ApiResponse;
import com.fishfrineds.api.dto.FishCatchDTO;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.User;
import com.fishfrineds.api.repository.UserRepository;
import com.fishfrineds.api.security.SecurityUtils;
import com.fishfrineds.api.service.FishCatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FishCatchController {
    
    private final FishCatchService fishCatchService;
    private final UserRepository userRepository;
    
    private Long getCurrentUserId() {
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) return null;
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElse(null);
    }
    
    @GetMapping
    public ApiResponse<PageResult<FishCatchDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(fishCatchService.findAll(pageable));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<FishCatchDTO> getById(@PathVariable Long id) {
        return fishCatchService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "鱼获记录不存在"));
    }
    
    @PostMapping
    public ApiResponse<FishCatchDTO> create(@Valid @RequestBody FishCatchDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return ApiResponse.success(fishCatchService.create(dto, userId));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<FishCatchDTO> update(@PathVariable Long id, @Valid @RequestBody FishCatchDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return fishCatchService.update(id, dto, userId, SecurityUtils.isAdmin())
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "鱼获记录不存在或无权修改"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        boolean deleted = fishCatchService.delete(id, userId, SecurityUtils.isAdmin());
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error(404, "鱼获记录不存在或无权删除");
    }
    
    @GetMapping("/search")
    public ApiResponse<PageResult<FishCatchDTO>> searchByType(
            @RequestParam String fishType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(fishCatchService.findByFishType(fishType, pageable));
    }
    
    @GetMapping("/fish-types")
    public ApiResponse<List<String>> getFishTypes() {
        return ApiResponse.success(fishCatchService.getDistinctFishTypes());
    }
}
