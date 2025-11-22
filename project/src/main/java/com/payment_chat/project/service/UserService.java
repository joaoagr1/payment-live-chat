package com.payment_chat.project.service;

import com.payment_chat.project.dto.UserDTO;
import com.payment_chat.project.model.User;
import com.payment_chat.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User getOrCreateUser(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject();

        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    // Log all available JWT claims for debugging
                    log.info("Creating new user from JWT. Available claims: {}", jwt.getClaims());

                    // Extract claims with fallbacks - try multiple claim names
                    String username = jwt.getClaimAsString("preferred_username");
                    if (username == null || username.isEmpty()) {
                        username = jwt.getClaimAsString("username");
                    }
                    if (username == null || username.isEmpty()) {
                        username = jwt.getClaimAsString("sub");
                    }

                    String email = jwt.getClaimAsString("email");
                    if (email == null || email.isEmpty()) {
                        email = jwt.getClaimAsString("email_verified");
                    }

                    String name = jwt.getClaimAsString("name");
                    if (name == null || name.isEmpty()) {
                        name = jwt.getClaimAsString("given_name");
                    }

                    log.info("Extracted claims - username: {}, email: {}, name: {}", username, email, name);

                    // Use keycloakId as fallback for username if not present
                    if (username == null || username.isEmpty()) {
                        username = keycloakId;
                        log.warn("Username was null/empty, using keycloakId as fallback: {}", username);
                    }

                    // Use fallback for email if not present or not verified in Keycloak
                    if (email == null || email.isEmpty()) {
                        email = username + "@local.invalid";
                        log.warn("Email was null/empty, using fallback: {}", email);
                    }

                    User newUser = User.builder()
                            .keycloakId(keycloakId)
                            .username(username)
                            .email(email)
                            .displayName(name)
                            .build();

                    log.info("About to save new user - keycloakId: {}, username: {}, email: {}, displayName: {}",
                             newUser.getKeycloakId(), newUser.getUsername(), newUser.getEmail(), newUser.getDisplayName());

                    return userRepository.save(newUser);
                });
    }

    public User getCurrentUser(Authentication authentication) {
        return getOrCreateUser(authentication);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void setUserOnlineStatus(User user, boolean online) {
        user.setOnline(online);
        userRepository.save(user);
    }

    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .keycloakId(user.getKeycloakId())
                .username(user.getUsername())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .online(user.isOnline())
                .build();
    }
}
