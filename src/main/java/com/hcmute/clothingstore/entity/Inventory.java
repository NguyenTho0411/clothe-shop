package com.hcmute.clothingstore.entity;


import com.hcmute.clothingstore.enumerated.InventoryChangeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory")
public class Inventory extends AbstractEntity{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_variant_id")
    private ProductVariant productVariant;
    @Column(nullable = false)
    private Integer changeInQuantity;

    @Column(nullable = false)
    private Integer quantityAfterChange;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryChangeType inventoryChangeType;

    @Column(nullable = false)
    private Instant timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
    private String notes;

}
