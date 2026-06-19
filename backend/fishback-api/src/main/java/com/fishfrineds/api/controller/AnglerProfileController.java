package com.fishfrineds.api.controller;

import com.fishfrineds.api.dto.AnglerProfileDTO;
import com.fishfrineds.api.dto.ApiResponse;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.User;
import com.fishfrineds.api.repository.UserRepository;
import com.fishfrineds.api.security.SecurityUtils;
import com.fishfrineds.api.service.AnglerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AnglerProfileController {
    
    private final AnglerProfileService anglerProfileService;
    private final UserRepository userRepository;
    
    private Long getCurrentUserId() {
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) return null;
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElse(null);
    }
    
    @GetMapping
    public ApiResponse<PageResult<AnglerProfileDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(anglerProfileService.findAll(pageable));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<AnglerProfileDTO> getById(@PathVariable Long id) {
        return anglerProfileService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "钓友信息不存在"));
    }
    
    @PostMapping
    public ApiResponse<AnglerProfileDTO> create(@Valid @RequestBody AnglerProfileDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return ApiResponse.success(anglerProfileService.create(dto, userId));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<AnglerProfileDTO> update(@PathVariable Long id, @Valid @RequestBody AnglerProfileDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return anglerProfileService.update(id, dto, userId, SecurityUtils.isAdmin())
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "钓友信息不存在或无权修改"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        boolean deleted = anglerProfileService.delete(id, userId, SecurityUtils.isAdmin());
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error(404, "钓友信息不存在或无权删除");
    }
    
    @GetMapping("/search")
    public ApiResponse<PageResult<AnglerProfileDTO>> search(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(anglerProfileService.searchByName(name, pageable));
    }
}
