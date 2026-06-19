package com.fishfrineds.api.controller;

import com.fishfrineds.api.dto.ApiResponse;
import com.fishfrineds.api.dto.LoginRequest;
import com.fishfrineds.api.dto.LoginResponse;
import com.fishfrineds.api.dto.RegisterRequest;
import com.fishfrineds.api.model.User;
import com.fishfrineds.api.repository.UserRepository;
import com.fishfrineds.api.security.JwtTokenProvider;
import com.fishfrineds.api.security.SecurityUtils;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

        private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtTokenProvider jwtTokenProvider;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UserRepository userRepository;

        @PostMapping("/register")
        public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
                logger.debug("Register attempt for user: {}", registerRequest.getUsername());

                // Check if username already exists
                if (userRepository.existsByUsername(registerRequest.getUsername())) {
                        return ResponseEntity.status(400).body(
                                        ApiResponse.error("用户名已存在"));
                }

                try {
                        // Create new user
                        User user = User.builder()
                                        .username(registerRequest.getUsername())
                                        .password(passwordEncoder.encode(registerRequest.getPassword()))
                                        .nickname(registerRequest.getNickname())
                                        .role("USER")
                                        .build();

                        userRepository.save(user);
                        logger.debug("User registered successfully: {}", registerRequest.getUsername());

                        // Auto login after registration
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        registerRequest.getUsername(),
                                                        registerRequest.getPassword()));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String jwt = jwtTokenProvider.generateToken(authentication);

                        LoginResponse response = LoginResponse.builder()
                                        .token(jwt)
                                        .tokenType("Bearer")
                                        .userId(user.getId())
                                        .username(authentication.getName())
                                        .role(authentication.getAuthorities().iterator().next().getAuthority()
                                                        .replace("ROLE_", ""))
                                        .message("注册成功")
                                        .build();

                        return ResponseEntity.ok(response);
                } catch (Exception e) {
                        logger.error("Registration error for user {}: {}", registerRequest.getUsername(),
                                        e.getMessage(), e);
                        return ResponseEntity.status(500).body(
                                        ApiResponse.error("注册失败: " + e.getMessage()));
                }
        }

        @PostMapping("/login")
        public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
                logger.debug("Login attempt for user: {}", loginRequest.getUsername());
                try {
                        Authentication authentication = authenticationManager.authenticate(
                                        new UsernamePasswordAuthenticationToken(
                                                        loginRequest.getUsername(),
                                                        loginRequest.getPassword()));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        String jwt = jwtTokenProvider.generateToken(authentication);

                        User loginUser = userRepository.findByUsername(authentication.getName()).orElse(null);
                        LoginResponse response = LoginResponse.builder()
                                        .token(jwt)
                                        .tokenType("Bearer")
                                        .userId(loginUser != null ? loginUser.getId() : null)
                                        .username(authentication.getName())
                                        .role(authentication.getAuthorities().iterator().next().getAuthority()
                                                        .replace("ROLE_", ""))
                                        .message("登录成功")
                                        .build();

                        logger.debug("Login successful for user: {}", loginRequest.getUsername());
                        return ResponseEntity.ok(response);
                } catch (BadCredentialsException e) {
                        logger.debug("Login failed for user {}: Bad credentials", loginRequest.getUsername());
                        return ResponseEntity.status(401).body(
                                        ApiResponse.error("用户名或密码错误"));
                } catch (Exception e) {
                        logger.error("Login error for user {}: {}", loginRequest.getUsername(), e.getMessage(), e);
                        return ResponseEntity.status(500).body(
                                        ApiResponse.error("登录失败: " + e.getMessage()));
                }
        }

        @GetMapping("/me")
        public ResponseEntity<?> getCurrentUser() {
                String username = SecurityUtils.getCurrentUsername();
                if (username == null) {
                        return ResponseEntity.status(401).body(ApiResponse.error("未登录"));
                }

                User user = userRepository.findByUsername(username)
                                .orElse(null);

                if (user == null) {
                        return ResponseEntity.status(404).body(ApiResponse.error("用户不存在"));
                }

                return ResponseEntity.ok(ApiResponse.success(Map.of(
                                "id", user.getId(),
                                "username", user.getUsername(),
                                "nickname", user.getNickname(),
                                "role", user.getRole(),
                                "avatar", user.getAvatar() != null ? user.getAvatar() : "")));
        }
}
