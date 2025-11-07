package com.ams.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@Entity
@Table(name = "users")
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;

	/** Unique system username (e.g., admin01, stu100, drv20) */
	@Column(unique = true, nullable = false, length = 100)
	private String username;

	/** Encrypted password */
	@Column(nullable = false)
	private String passwordHash;

	/** User role (ADMIN / STUDENT / DRIVER) */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Role role;

	/** Link to contact info (email / phone) */
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "contact_id", referencedColumnName = "contactId")
	private Contact contact;

	/** Login/account status */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private AccountStatus status = AccountStatus.ACTIVE;

	/** Audit fields */
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = createdAt;
		if (status == null)
			status = AccountStatus.ACTIVE;
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public enum Role {
		ADMIN, STUDENT, DRIVER;

		/**
		 * ✅ Allows case-insensitive JSON role parsing. Example: "admin", "Admin", or
		 * "ADMIN" → ADMIN
		 */
		@JsonCreator
		public static Role fromString(String value) {
			if (value == null)
				return null;
			try {
				return Role.valueOf(value.trim().toUpperCase());
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Invalid role: " + value + ". Allowed values: ADMIN, STUDENT, DRIVER.");
			}
		}

		/**
		 * ✅ Ensures enum serializes as uppercase string in JSON
		 */
		@JsonValue
		public String toValue() {
			return this.name();
		}
	}

	/** Enum for login/account status */
	public enum AccountStatus {
		ACTIVE, // Can log in and use the system
		INACTIVE, // Account created but not activated yet
		SUSPENDED, // Temporarily disabled by admin
		LOCKED, // Too many failed login attempts
		DELETED // Soft-deleted or removed
	}
}
