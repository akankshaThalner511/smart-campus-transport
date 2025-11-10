package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "bus_route")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusRoute extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "route_id", updatable = false, nullable = false)
    private UUID routeId;

    @Column(name = "route_name")
    private String routeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_stop_id")
    private BusStop startStop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_stop_id")
    private BusStop endStop;
}
