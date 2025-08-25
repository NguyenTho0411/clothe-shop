package com.hcmute.clothingstore.entity;


import com.hcmute.clothingstore.enumerated.DeliveryMethod;
import com.hcmute.clothingstore.enumerated.EOrderStatus;
import com.hcmute.clothingstore.enumerated.EPaymentMethod;
import com.hcmute.clothingstore.enumerated.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order extends AbstractEntity{

    private String code;
    private Instant orderDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id")
    private CustomerAccount customer;

    @Embedded
    private ShippingImformation shippingImformation;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;


    private Double total;
    private Double shippingFee;
    private Double discount;
    private Double finalDiscount;

    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private EOrderStatus orderStatus;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private Instant paymentDate;

    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;


    @Column(name = "points_used")
    private Long pointUsed = 0L;

    @Column(name = "point_earned")
    private Long pointEarned = 0L;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointHistory> pointHistories = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String cancelReason;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private ReturnRequest returnRequest;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistory> orderStatusHistories = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class ShippingImformation{
        private String firstName; private String lastName;

        private String fullName;

        private String phoneNumber;

        private String address;

        private String country;

        private Long wardId; private String ward;

        private Long provinceID;private String province;

        private Long districtId;private String district;

    }
}
