package com.hcmute.clothingstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="permissions")
public class Permission extends AbstractEntity{

    private String name;

    private String apiPath;

    private String method;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Role> role;


    public Permission(String name, String apiPath, String method) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
    }
}
