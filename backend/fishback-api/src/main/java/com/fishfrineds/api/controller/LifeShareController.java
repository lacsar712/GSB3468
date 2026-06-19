package com.fishfrineds.api.controller;

import com.fishfrineds.api.dto.ApiResponse;
import com.fishfrineds.api.dto.LifeShareDTO;
import com.fishfrineds.api.dto.PageResult;
import com.fishfrineds.api.model.User;
import com.fishfrineds.api.repository.UserRepository;
import com.fishfrineds.api.security.SecurityUtils;
import com.fishfrineds.api.service.LifeShareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/life")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LifeShareController {
    
    private final LifeShareService lifeShareService;
    private final UserRepository userRepository;
    
    private Long getCurrentUserId() {
        String username = SecurityUtils.getCurrentUsername();
        if (username == null) return null;
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElse(null);
    }
    
    @GetMapping
    public ApiResponse<PageResult<LifeShareDTO>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(lifeShareService.findAll(pageable));
    }
    
    @GetMapping("/{id}")
    public ApiResponse<LifeShareDTO> getById(@PathVariable Long id) {
        lifeShareService.incrementViewCount(id);
        return lifeShareService.findById(id)
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "生活分享不存在"));
    }
    
    @PostMapping
    public ApiResponse<LifeShareDTO> create(@Valid @RequestBody LifeShareDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return ApiResponse.success(lifeShareService.create(dto, userId));
    }
    
    @PutMapping("/{id}")
    public ApiResponse<LifeShareDTO> update(@PathVariable Long id, @Valid @RequestBody LifeShareDTO dto) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        return lifeShareService.update(id, dto, userId, SecurityUtils.isAdmin())
                .map(ApiResponse::success)
                .orElse(ApiResponse.error(404, "生活分享不存在或无权修改"));
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ApiResponse.error(401, "请先登录");
        }
        boolean deleted = lifeShareService.delete(id, userId, SecurityUtils.isAdmin());
        if (deleted) {
            return ApiResponse.success();
        }
        return ApiResponse.error(404, "生活分享不存在或无权删除");
    }
}
