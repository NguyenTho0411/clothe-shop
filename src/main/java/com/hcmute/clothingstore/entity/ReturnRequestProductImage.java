package com.hcmute.clothingstore.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "return_request_images")
public class ReturnRequestProductImage extends AbstractEntity{
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "return_request_id")
    private ReturnRequest returnRequest;
}
