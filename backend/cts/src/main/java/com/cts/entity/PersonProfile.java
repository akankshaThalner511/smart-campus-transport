package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import com.cts.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "person_profile")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonProfile extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "person_id", updatable = false, nullable = false)
    private UUID personId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "dob")
    private String dob; // ISO date string or use LocalDate

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private ContactInfo contact;
}
