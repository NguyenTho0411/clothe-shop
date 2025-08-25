package com.hcmute.clothingstore.entity;


import com.hcmute.clothingstore.enumerated.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "notifications",
            indexes = {@Index(name = "idx_notification_user", columnList = "user_id"),
                    @Index(name = "idx_notification_type", columnList = "type"),
                    @Index(name="idx_notification_is_read", columnList = "is_read")
            })
public class Notification extends AbstractEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private NotificationType notificationType;
    @Column(nullable = false, name="is_read")
    private boolean isRead= false;

    @Column(nullable = false)
    private Instant notificationDate;

    @Column(name="reference_ids")
    private String referenceIds;

    private Instant promotionStartDate;

    private Instant promotionEndDate;
}
