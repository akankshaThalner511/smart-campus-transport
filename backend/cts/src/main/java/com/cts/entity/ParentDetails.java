package com.cts.entity;

import com.cts.entity.BaseAuditEntity;
import com.cts.entity.enums.ParentPriority;
import com.cts.entity.enums.RelationType;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name = "parent_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentDetails extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name="UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "parent_id", updatable = false, nullable = false)
    private UUID parentId;

    // optional login
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private PersonProfile person;

    @Enumerated(EnumType.STRING)
    private RelationType relation;

    @Enumerated(EnumType.STRING)
    private ParentPriority priority;

    @Column(name = "alerts_enabled")
    private Boolean alertsEnabled = true;
}
