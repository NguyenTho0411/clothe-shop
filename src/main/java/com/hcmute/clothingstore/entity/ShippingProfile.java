package com.hcmute.clothingstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="shipping_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingProfile extends AbstractEntity{
    private String firstName; private String lastName;

    private String phoneNumber;
    private String address;

    private Long wardId; private String ward;

    private Long districtId; private String district;

    private Long provinceId; private String province;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="customer_id", nullable = false,referencedColumnName = "user_id")
    private CustomerAccount customer;

}
