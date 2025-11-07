package com.ams.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ams.entity.Student;
import com.ams.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	/** Find student by unique studentId */
	Optional<Student> findByStudentId(String studentId);

	/** Find student by username (through linked User) */
	Optional<Student> findByUser_Username(String username);

	/** Find students by department */
	List<Student> findByDepartment(String department);

	/** Find students by status (ACTIVE / INACTIVE / etc.) */
	List<Student> findByStatus(Student.Status status);

	/** Find students who have paid / unpaid fees */
	List<Student> findByFeeStatus(Student.FeeStatus feeStatus);

	/** Find students by contact info (email or phone) */
	Optional<Student> findByContact_EmailId(String emailId);

	Optional<Student> findByContact_MobileNo(String mobileNo);

	Optional<Student> findByUser(User user);

}
