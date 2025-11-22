package com.payment_chat.project.service;

import com.payment_chat.project.dto.FriendshipDTO;
import com.payment_chat.project.model.Friendship;
import com.payment_chat.project.model.Friendship.FriendshipStatus;
import com.payment_chat.project.model.User;
import com.payment_chat.project.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    @Transactional
    public FriendshipDTO sendFriendRequest(User requester, String addresseeUsername) {
        User addressee = userService.findByUsername(addresseeUsername);

        if (requester.getId().equals(addressee.getId())) {
            throw new RuntimeException("Cannot send friend request to yourself");
        }

        friendshipRepository.findBetweenUsers(requester, addressee)
                .ifPresent(f -> {
                    throw new RuntimeException("Friendship already exists");
                });

        Friendship friendship = Friendship.builder()
                .requester(requester)
                .addressee(addressee)
                .status(FriendshipStatus.PENDING)
                .build();

        return toDTO(friendshipRepository.save(friendship));
    }

    @Transactional
    public FriendshipDTO acceptFriendRequest(Long friendshipId, User currentUser) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));

        if (!friendship.getAddressee().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to accept this request");
        }

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new RuntimeException("Request is not pending");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendship.setAcceptedAt(LocalDateTime.now());

        return toDTO(friendshipRepository.save(friendship));
    }

    @Transactional
    public void rejectFriendRequest(Long friendshipId, User currentUser) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Friendship not found"));

        if (!friendship.getAddressee().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Not authorized to reject this request");
        }

        friendship.setStatus(FriendshipStatus.REJECTED);
        friendshipRepository.save(friendship);
    }

    public List<FriendshipDTO> getFriends(User user) {
        return friendshipRepository.findByUserAndStatus(user, FriendshipStatus.ACCEPTED)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<FriendshipDTO> getPendingRequests(User user) {
        return friendshipRepository.findPendingRequestsForUser(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public boolean areFriends(User user1, User user2) {
        return friendshipRepository.findBetweenUsers(user1, user2)
                .map(f -> f.getStatus() == FriendshipStatus.ACCEPTED)
                .orElse(false);
    }

    private FriendshipDTO toDTO(Friendship friendship) {
        return FriendshipDTO.builder()
                .id(friendship.getId())
                .requester(userService.toDTO(friendship.getRequester()))
                .addressee(userService.toDTO(friendship.getAddressee()))
                .status(friendship.getStatus())
                .createdAt(friendship.getCreatedAt())
                .acceptedAt(friendship.getAcceptedAt())
                .build();
    }
}
