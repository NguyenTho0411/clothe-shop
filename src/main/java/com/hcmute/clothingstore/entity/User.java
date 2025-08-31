package com.hcmute.clothingstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.List;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User extends AbstractEntity{

    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;

    @Size(max = 50)
    @JsonIgnore
    @Column(name="activation_key", length = 50)
    private String activationKey;

    @Size(max=6)
    private String activationCode;

    @Column(name = "activation_code_date")
    private Instant activationCodeDate = null;

    @JsonIgnore
    @Size(max = 50)
    @Column(name = "reset_key", length=50)
    private String resetKey;

    @Column(name="reset_date")
    private Instant resetDate = null;
    @Size(max=6)
    private String resetCode;

    private Instant codeResetDate = null;

    private String profileCode;

    private Instant profileCodeDate = null;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRefreshToken> userRefreshTokens;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomerAccount customerAccount;

    @Column(name="google_id")
    private String googleId;

    public User(String email, String password, boolean activated) {
        this.email = email;
        this.password = password;
        this.activated = activated;
    }
}
