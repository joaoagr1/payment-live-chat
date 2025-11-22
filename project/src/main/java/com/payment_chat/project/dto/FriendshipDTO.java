package com.payment_chat.project.dto;

import com.payment_chat.project.model.Friendship.FriendshipStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FriendshipDTO {
    private Long id;
    private UserDTO requester;
    private UserDTO addressee;
    private FriendshipStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;
}
