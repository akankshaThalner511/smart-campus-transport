package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import com.cts.entity.enums.DriverStatus;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "driver_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DriverDetails extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "driver_id", updatable = false, nullable = false)
    private UUID driverId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "license_number", unique = true)
    private String licenseNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_bus_id")
    private BusInfo assignedBus;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;
}
