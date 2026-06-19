package com.fishfrineds.api.controller;

import com.fishfrineds.api.dto.ApiResponse;
import com.fishfrineds.api.dto.GearSaleDTO;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.User;
import com.fishfrineds.api.repository.UserRepository;
import com.fishfrineds.api.security.SecurityUtils;
import com.fishfrineds.api.service.GearSaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gear")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GearSaleController {
    
    private final GearSaleService gearSaleService;
    private final UserRepository userRepository;
    
    private Long getCurrentUserId() {
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) return null;
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElse(null);
    }
    
    @GetMapping
    public ApiResponse<PageResult<GearSaleDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(gearSaleService.findAll(pageable));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<GearSaleDTO> getById(@PathVariable Long id) {
        return gearSaleService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "钓具信息不存在"));
    }
    
    @PostMapping
    public ApiResponse<GearSaleDTO> create(@Valid @RequestBody GearSaleDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return ApiResponse.success(gearSaleService.create(dto, userId));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<GearSaleDTO> update(@PathVariable Long id, @Valid @RequestBody GearSaleDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return gearSaleService.update(id, dto, userId, SecurityUtils.isAdmin())
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "钓具信息不存在或无权修改"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        boolean deleted = gearSaleService.delete(id, userId, SecurityUtils.isAdmin());
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error(404, "钓具信息不存在或无权删除");
    }
    
    @GetMapping("/category/{category}")
    public ApiResponse<PageResult<GearSaleDTO>> listByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(gearSaleService.findByCategory(category, pageable));
    }
}
