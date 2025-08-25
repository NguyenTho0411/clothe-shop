package com.hcmute.clothingstore.entity;


import com.hcmute.clothingstore.enumerated.EColor;
import com.hcmute.clothingstore.enumerated.ESize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;

@Entity
@Table(name="product_variants", indexes = {
        @Index(name = "idx_variant_price_diff", columnList = "differentPrice"),
        @Index(name="idx_variant_price", columnList = "product_id,differentPrice"),
        @Index(name="idx_variant_is_deleted", columnList = "isDeleted")
})
@SQLDelete(sql= "UPDATE product_variants SET is_deleted = false where id = ?")
@Where(clause = "is_deleted = false")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariant extends SoftDeleteEntity{

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(name = "sku", unique = true)
    private String sku;

    @Enumerated(EnumType.STRING)
    private ESize size;
    @Enumerated(EnumType.STRING)
    private EColor color;
    private Integer quantity;
    private Double differentPrice;

    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name="deletedFilter", condition = "is_deleted= : isDeleted")
    private List<ProductImage> productImageList;

    @Version
    @ColumnDefault("0")
    private Long version;

}
