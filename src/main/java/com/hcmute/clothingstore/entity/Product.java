package com.hcmute.clothingstore.entity;


import com.hcmute.clothingstore.enumerated.EColor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Table(name="products", indexes = {
        @Index(name="idx_product_price", columnList = "price"),
        @Index(name="idx_product_featured", columnList = "isFeatured"),
        @Index(name="idx_product_created_at", columnList = "createdAt"),
        @Index(name="idx_product_is_deleted", columnList = "isDeleted")
})
@SQLDelete(sql ="Update products SET is_deleted = 1 where id =?")
@Where(clause = "is_deleted = false")
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
public class Product extends  SoftDeleteEntity{
    
    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    private Double price;

    private String slug;


    private boolean isFeatured;

    @Enumerated(EnumType.STRING)
    private EColor colorDefault;

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName = "id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("image_order ASC")
    @Filter(name="deletedFilter", condition = "is_deleted = :isDeleted")
    private List<ProductImage> images;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name="deletedFilter" , condition = "is_deleted= :isDeleted")
    private List<ProductVariant> productVariants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name="deletedFilter", condition = "is_deleted = : isDeleted")
    private List<Review> reviews;

    @ManyToMany(mappedBy = "products")
    private List<Promotion> promotions;

}
