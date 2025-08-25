package com.hcmute.clothingstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AbstractEntity{
    @Column(nullable = false)
    private String name; //CUSTOMER //STAFF //ADMIN
    private String description;
    private boolean active;


    @ManyToMany
    @JsonIgnoreProperties(value = {"roles"})
    @JoinTable(name="permission_role", joinColumns = @JoinColumn(name="role_id"),
            inverseJoinColumns = @JoinColumn(name="permission_id"))
    private List<Permission> permissions;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> users;

    public Role(String name, String description, boolean active, List<Permission> permissions) {
        this.name = name;
        this.description = description;
        this.active = active;
        this.permissions = permissions;
    }
}
