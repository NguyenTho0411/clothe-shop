package com.hcmute.clothingstore.entity;


import com.hcmute.clothingstore.enumerated.PointActionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="point_histories")
public class PointHistory extends AbstractEntity{


    private Long points;

    @Enumerated(EnumType.STRING)
    @Column(name="action_type")
    private PointActionType actionType;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id", nullable = false,referencedColumnName = "user_id")
    private CustomerAccount customerAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", referencedColumnName = "id")
    private Order order;

    @OneToOne(fetch = FetchType.LAZY)
    private Review review;
}
