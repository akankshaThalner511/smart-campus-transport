package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import com.cts.entity.enums.FeeStatus;
import com.cts.entity.enums.StudentStatus;
import com.cts.entity.enums.TransportValidation;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "student_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetails extends BaseAuditEntity {

    // Primary identifier (UUID)
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "student_id", updatable = false, nullable = false)
    private UUID studentId;

    // Link to the user account
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "roll_no")
    private String rollNo;

    private String department;

    @Enumerated(EnumType.STRING)
    private FeeStatus feeStatus;

    @Enumerated(EnumType.STRING)
    private TransportValidation transportValidation;

    // pickup location
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stop_id")
    private BusStop stop;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Enumerated(EnumType.STRING)
    private StudentStatus status;
}
