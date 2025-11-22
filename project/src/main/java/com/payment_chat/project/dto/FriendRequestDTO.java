package com.payment_chat.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FriendRequestDTO {
    @NotBlank(message = "Username is required")
    private String username;
}
