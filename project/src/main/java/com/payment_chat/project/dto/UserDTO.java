package com.payment_chat.project.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String keycloakId;
    private String username;
    private String email;
    private String displayName;
    private String avatarUrl;
    private boolean online;
}
