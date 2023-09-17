package com.tma.english.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
@MappedSuperclass
public abstract class BaseEntity {
    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @PrePersist
    public void onInsert() {
        createdAt = Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
    }
}
