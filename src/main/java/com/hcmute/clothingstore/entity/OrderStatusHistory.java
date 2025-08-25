package com.hcmute.clothingstore.entity;

import com.hcmute.clothingstore.enumerated.EOrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_status_histories")
public class OrderStatusHistory extends AbstractEntity{
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private EOrderStatus previousStatus;


    @Enumerated(EnumType.STRING)
    private EOrderStatus newStatus;


    private Date updatedTimeStamp;

    private String updatedBy;

    private String note;


}
