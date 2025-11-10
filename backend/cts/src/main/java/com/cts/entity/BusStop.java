package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "bus_stop")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusStop extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "stop_id", updatable = false, nullable = false)
    private UUID stopId;

    @Column(name = "stop_name")
    private String stopName;

    private Double latitude;
    private Double longitude;
}
