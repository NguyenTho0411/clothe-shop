package com.hcmute.clothingstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
@Table(name="carts")
public class Cart extends AbstractEntity{

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "customer_id", unique = true, nullable = false,referencedColumnName = "user_id")
    private CustomerAccount customer;

    @OneToMany(mappedBy = "cart",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;
}
