package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "student_parent_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentParentMapping extends BaseAuditEntity {

    @EmbeddedId
    private StudentParentKey id;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @Column(name = "created_at", insertable = true, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
