package com.hcmute.clothingstore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Entity
@Getter
@Setter
@Table(name="user_refresh_tokens")
@NoArgsConstructor
@AllArgsConstructor
public class UserRefreshToken{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;

    @Column(name="created_date")
    private Instant createdDate;

    @Column(name = "expiry_date")
    private Instant expiryDate;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
