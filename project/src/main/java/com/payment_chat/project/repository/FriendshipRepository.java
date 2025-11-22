package com.payment_chat.project.repository;

import com.payment_chat.project.model.Friendship;
import com.payment_chat.project.model.Friendship.FriendshipStatus;
import com.payment_chat.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship f WHERE (f.requester = :user OR f.addressee = :user) AND f.status = :status")
    List<Friendship> findByUserAndStatus(User user, FriendshipStatus status);

    @Query("SELECT f FROM Friendship f WHERE f.addressee = :user AND f.status = 'PENDING'")
    List<Friendship> findPendingRequestsForUser(User user);

    @Query("SELECT f FROM Friendship f WHERE ((f.requester = :user1 AND f.addressee = :user2) OR (f.requester = :user2 AND f.addressee = :user1))")
    Optional<Friendship> findBetweenUsers(User user1, User user2);
}
