package com.fishfrineds.api.controller;

import com.fishfrineds.api.dto.ApiResponse;
import com.fishfrineds.api.dto.FishingSpotDTO;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.User;
import com.fishfrineds.api.repository.UserRepository;
import com.fishfrineds.api.security.SecurityUtils;
import com.fishfrineds.api.service.FishingSpotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FishingSpotController {
    
    private final FishingSpotService fishingSpotService;
    private final UserRepository userRepository;
    
    private Long getCurrentUserId() {
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) return null;
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElse(null);
    }
    
    @GetMapping
    public ApiResponse<PageResult<FishingSpotDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(fishingSpotService.findAll(pageable));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<FishingSpotDTO> getById(@PathVariable Long id) {
        return fishingSpotService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "钓点不存在"));
    }
    
    @PostMapping
    public ApiResponse<FishingSpotDTO> create(@Valid @RequestBody FishingSpotDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return ApiResponse.success(fishingSpotService.create(dto, userId));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<FishingSpotDTO> update(@PathVariable Long id, @Valid @RequestBody FishingSpotDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return fishingSpotService.update(id, dto, userId, SecurityUtils.isAdmin())
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "钓点不存在"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        boolean deleted = fishingSpotService.delete(id, userId, SecurityUtils.isAdmin());
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error(404, "钓点不存在");
    }
    
    @GetMapping("/search")
    public ApiResponse<PageResult<FishingSpotDTO>> search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(fishingSpotService.searchByTitle(keyword, pageable));
    }
}
