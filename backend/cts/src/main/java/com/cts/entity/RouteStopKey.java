package com.cts.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteStopKey implements Serializable {
    private UUID routeId;
    private UUID stopId;
}
