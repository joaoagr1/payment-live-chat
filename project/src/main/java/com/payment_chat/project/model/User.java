package com.payment_chat.project.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String keycloakId;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String displayName;

    private String avatarUrl;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    private boolean online = false;

    @OneToMany(mappedBy = "requester")
    @Builder.Default
    private Set<Friendship> sentRequests = new HashSet<>();

    @OneToMany(mappedBy = "addressee")
    @Builder.Default
    private Set<Friendship> receivedRequests = new HashSet<>();
}
