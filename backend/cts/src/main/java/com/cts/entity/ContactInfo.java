package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "contact_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInfo extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "contact_id", updatable = false, nullable = false)
    private UUID contactId;

    @Column(name = "mobile_no", length = 20)
    private String mobileNo;

    @Column(name = "email_id", unique = true)
    private String emailId;

    @Column(name = "address", length = 1024)
    private String address;
}
