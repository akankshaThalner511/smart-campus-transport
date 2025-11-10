package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "bus_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusInfo extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "bus_id", updatable = false, nullable = false)
    private UUID busId;

    @Column(name = "bus_number", unique = true)
    private String busNumber;

    private Integer capacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private BusRoute route;
}
