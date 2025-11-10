package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "admin_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDetails extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "admin_id", updatable = false, nullable = false)
    private UUID adminId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "full_name")
    private String fullName;
}
