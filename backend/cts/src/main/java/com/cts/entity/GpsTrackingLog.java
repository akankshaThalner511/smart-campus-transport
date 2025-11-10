package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "gps_tracking_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GpsTrackingLog extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "gps_id", updatable = false, nullable = false)
    private UUID gpsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private DailyTrip trip;

    private Double latitude;
    private Double longitude;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;
}
