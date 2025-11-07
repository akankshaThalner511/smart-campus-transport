package com.ams.entity;

import java.time.LocalDateTime;

import org.springframework.util.RouteMatcher.Route;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bus")
@Data
public class Bus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String busId;

	@Column(unique = true, nullable = false)
	private String busNumber;

	private Integer capacity;

//	@ManyToOne
//	@JoinColumn(name = "route_id")
//	private Route route;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// getters and setters
}
