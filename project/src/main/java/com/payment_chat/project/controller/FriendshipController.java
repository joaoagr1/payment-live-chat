package com.payment_chat.project.controller;

import com.payment_chat.project.dto.FriendRequestDTO;
import com.payment_chat.project.dto.FriendshipDTO;
import com.payment_chat.project.model.User;
import com.payment_chat.project.service.FriendshipService;
import com.payment_chat.project.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;
    private final UserService userService;

    @PostMapping("/request")
    public ResponseEntity<FriendshipDTO> sendFriendRequest(
            @Valid @RequestBody FriendRequestDTO request,
            Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        FriendshipDTO friendship = friendshipService.sendFriendRequest(currentUser, request.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(friendship);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<FriendshipDTO> acceptFriendRequest(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        FriendshipDTO friendship = friendshipService.acceptFriendRequest(id, currentUser);
        return ResponseEntity.ok(friendship);
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectFriendRequest(
            @PathVariable Long id,
            Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        friendshipService.rejectFriendRequest(id, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/friends")
    public ResponseEntity<List<FriendshipDTO>> getFriends(Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        List<FriendshipDTO> friends = friendshipService.getFriends(currentUser);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/requests/pending")
    public ResponseEntity<List<FriendshipDTO>> getPendingRequests(Authentication authentication) {
        User currentUser = userService.getCurrentUser(authentication);
        List<FriendshipDTO> requests = friendshipService.getPendingRequests(currentUser);
        return ResponseEntity.ok(requests);
    }
}
