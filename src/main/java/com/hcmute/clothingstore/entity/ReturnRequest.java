package com.hcmute.clothingstore.entity;


import com.hcmute.clothingstore.enumerated.ECashBackStatus;
import com.hcmute.clothingstore.enumerated.EPaymentMethod;
import com.hcmute.clothingstore.enumerated.ReturnRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="return_requests")
public class ReturnRequest extends AbstractEntity{

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name="customer_id", nullable = false, referencedColumnName = "user_id")
    private CustomerAccount customer;

    @Enumerated(EnumType.STRING)
    private ECashBackStatus cashBackStatus;

    @Enumerated(EnumType.STRING)
    private ReturnRequestStatus returnRequestStatus;

    @Enumerated(EnumType.STRING)
    private EPaymentMethod originalPaymentMethod;

    private String bankName;

    private String accountNumber;

    private String accountBankName;

    @Column(columnDefinition = "TEXT")
    private String adminComment;

    @OneToMany(mappedBy = "returnRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReturnRequestProductImage> returnRequestProductImageList = new ArrayList<>();
}
