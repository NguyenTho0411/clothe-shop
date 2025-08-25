package com.hcmute.clothingstore.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion extends AbstractEntity{
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Double discountRate;
    private Instant startDate;
    private Instant endDate;
    private String imageUrl;

    @ManyToMany
    @JoinTable(name="promotion_product", joinColumns = @JoinColumn(name = "promotion_id"),
            inverseJoinColumns = @JoinColumn(name="product_id"),
            indexes = {@Index(name="idx_promo_product", columnList ="product_id,promotion_id")})
    private List<Product> products;

    @ManyToMany
    @JoinTable(name="promotion_category", joinColumns = @JoinColumn(name = "promotion_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"),
    indexes = {@Index(name = "idx_promo_category", columnList = "category_id,promotion_id")})
    private List<Category> categories;


}
