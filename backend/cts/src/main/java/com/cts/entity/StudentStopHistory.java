package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_stop_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentStopHistory extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private StudentDetails student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "old_stop_id")
    private BusStop oldStop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "new_stop_id")
    private BusStop newStop;

    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(name = "changed_by")
    private UUID changedBy; // user id who changed
}
