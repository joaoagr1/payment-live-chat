package com.payment_chat.project.repository;

import com.payment_chat.project.model.Message;
import com.payment_chat.project.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.recipient = :user2) OR (m.sender = :user2 AND m.recipient = :user1) ORDER BY m.sentAt DESC")
    Page<Message> findConversation(User user1, User user2, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.recipient = :user AND m.readAt IS NULL")
    java.util.List<Message> findUnreadMessages(User user);
}
