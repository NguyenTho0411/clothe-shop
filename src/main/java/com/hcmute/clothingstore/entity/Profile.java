package com.hcmute.clothingstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmute.clothingstore.enumerated.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="profiles")
public class Profile extends AbstractEntity{

    private String firstName;
    private String lastName;

    private String fullName;

    private LocalDate birthDate;

    private String phoneNumber;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String avatar;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name="user_id" , unique = true)
    private User user;
}
