package com.payment_chat.project.service;

import com.payment_chat.project.dto.UserDTO;
import com.payment_chat.project.model.User;
import com.payment_chat.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User getOrCreateUser(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject();

        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    // Extract claims with fallbacks
                    String username = jwt.getClaimAsString("preferred_username");
                    String email = jwt.getClaimAsString("email");
                    String name = jwt.getClaimAsString("name");

                    // Use keycloakId as fallback for username if not present
                    if (username == null || username.isEmpty()) {
                        username = keycloakId;
                    }

                    User newUser = User.builder()
                            .keycloakId(keycloakId)
                            .username(username)
                            .email(email)
                            .displayName(name)
                            .build();
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
