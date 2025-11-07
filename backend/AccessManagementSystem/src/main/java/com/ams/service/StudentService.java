package com.ams.service;

import com.ams.entity.Student;
import com.ams.entity.User;
import com.ams.repositories.StudentRepository;
import com.ams.service.base.AbstractPersonService;
import com.ams.utility.QRCodeUtil;
import com.google.zxing.WriterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class StudentService extends AbstractPersonService {

	private final StudentRepository studentRepository;
	private final QRCodeUtil qrCodeUtil;

	public StudentService(UserService userService, ContactService contactService, StudentRepository studentRepository,
			QRCodeUtil qrCodeUtil) {
		super(userService, contactService);
		this.studentRepository = studentRepository;
		this.qrCodeUtil = qrCodeUtil;
	}

	/**
	 * Creates a new Student with generated User, Contact, and QR code.
	 */
	@Transactional
	public Student createStudent(Student student, String password) {
		log.info("Creating student: {} - {}", student.getStudentId(), student.getFullName());

		// Contact handling
		var contact = Optional.ofNullable(student.getContact())
				.map(c -> contactService.createOrGetContact(c.getMobileNo(), c.getEmailId())).orElse(null);

		// Create User for Student
		var user = userService.createUser(student.getStudentId(), password, User.Role.STUDENT, contact);
		student.setUser(user);
		student.setContact(contact);

		// Save student
		Student saved = studentRepository.save(student);

		// Generate and attach QR code
		try {
			String qrUrl = qrCodeUtil.generateQRCode(saved.getStudentId());
			saved.setQrImageUrl(qrUrl);
			saved = studentRepository.save(saved);
		} catch (WriterException | IOException e) {
			log.error("QR code generation failed for student {}: {}", saved.getStudentId(), e.getMessage(), e);
			throw new RuntimeException("QR code generation failed for student " + saved.getStudentId(), e);
		}

		return saved;
	}

	public Student getStudentByStudentId(String studentId) {
		return studentRepository.findByStudentId(studentId)
				.orElseThrow(() -> new RuntimeException("Student not found: " + studentId));
	}

	public Student getStudentByUser(User user) {
		return studentRepository.findByUser(user)
				.orElseThrow(() -> new RuntimeException("Student not found for user: " + user.getUsername()));
	}
}
