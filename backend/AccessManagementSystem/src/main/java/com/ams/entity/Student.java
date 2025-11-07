package com.ams.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
@Data
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String fullName;

    private String department;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FeeStatus feeStatus = FeeStatus.UNPAID;

    private String profileImageUrl;

    private String pickupLoc;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    /** 
     * Automatically set after QR generation 
     * Example: http://localhost:8080/api/qr/STU101.png 
     */
    private String qrImageUrl;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum FeeStatus {
        PAID, UNPAID
    }

    public enum Status {
        ACTIVE, INACTIVE, SUSPENDED, GRADUATED
    }
}
