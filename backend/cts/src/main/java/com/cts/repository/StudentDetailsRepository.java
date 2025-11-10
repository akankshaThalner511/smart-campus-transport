package com.cts.repository;

import com.cts.entity.StudentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StudentDetailsRepository extends JpaRepository<StudentDetails, UUID> {
}
