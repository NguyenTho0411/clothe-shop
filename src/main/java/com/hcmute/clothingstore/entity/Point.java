package com.hcmute.clothingstore.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "points")
@NoArgsConstructor
@AllArgsConstructor
public class Point extends AbstractEntity{

    @Column(name="current_points")
    private Long currentPoints = 0L;

    @Column(name="total_accummulated_points")
    private Long totalAccumulatedPoints = 0L;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", nullable = false)
    private CustomerAccount customer;
}
