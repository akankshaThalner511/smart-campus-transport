package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "route_stop_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStopMapping extends BaseAuditEntity {

    @EmbeddedId
    private RouteStopKey id;

    @Column(name = "sequence_number")
    private Integer sequenceNumber;
}
