package com.fishfrineds.api.controller.admin;

import com.fishfrineds.api.dto.ApiResponse;
import com.fishfrineds.api.model.User;
import com.fishfrineds.api.repository.FishingSpotRepository;
import com.fishfrineds.api.repository.FishCatchRepository;
import com.fishfrineds.api.repository.LifeShareRepository;
import com.fishfrineds.api.repository.AnglerProfileRepository;
import com.fishfrineds.api.repository.GearSaleRepository;
import com.fishfrineds.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserRepository userRepository;
    private final FishingSpotRepository fishingSpotRepository;
    private final FishCatchRepository fishCatchRepository;
    private final LifeShareRepository lifeShareRepository;
    private final AnglerProfileRepository anglerProfileRepository;
    private final GearSaleRepository gearSaleRepository;

    @GetMapping("/users")
    public ApiResponse<Page<User>> listUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ApiResponse.success(userRepository.findAll(pageable));
    }

    @PutMapping("/users/{id}/role")
    public ApiResponse<Void> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ApiResponse.error(404, "用户不存在");
        }
        user.setRole(role);
        userRepository.save(user);
        return ApiResponse.success();
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ApiResponse.error(404, "用户不存在");
        }
        userRepository.deleteById(id);
        return ApiResponse.success();
    }

    @GetMapping("/stats")
    public ApiResponse<Map<String, Long>> getStats() {
        Map<String, Long> stats = Map.of(
                "users", userRepository.count(),
                "spots", fishingSpotRepository.countByIsDeletedFalse(),
                "catches", fishCatchRepository.countByIsDeletedFalse(),
                "lifeShares", lifeShareRepository.countByIsDeletedFalse(),
                "friends", anglerProfileRepository.count(),
                "gear", gearSaleRepository.count()
        );
        return ApiResponse.success(stats);
    }
}
