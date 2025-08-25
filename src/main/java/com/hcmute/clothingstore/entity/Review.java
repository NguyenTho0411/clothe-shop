package com.hcmute.clothingstore.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Table(name="reviews",indexes = {
        @Index(name = "idx_review_rating", columnList = "product_id,rating"),
        @Index(name="idx_review_is_deleted", columnList = "isDeleted")})
@SQLDelete(sql = "UPDATE reviews SET is_deleted = 1 where id = ?")
@Where(clause = "is_deleted = false")
@AllArgsConstructor
@NoArgsConstructor
public class Review extends SoftDeleteEntity{

    private String description;
    private Double rating;


    private boolean published= true;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private CustomerAccount customer;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
    private Long pointsEarned = 0L;

    @OneToOne(mappedBy = "review")
    private PointHistory pointHistory;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    @Column(length=1000)
    private String videoUrl;
}
