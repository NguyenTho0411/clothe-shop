package com.hcmute.clothingstore.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="categories")
public class Category extends AbstractEntity{
    private String name;
    private String imageUrl;

    @OneToMany(mappedBy = "category")
    private List<Product> products;

    @ManyToMany(mappedBy = "categories")
    private List<Promotion> promotions;
}
