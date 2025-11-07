package com.ams.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.*;

import com.ams.entity.Contact;
import com.ams.entity.Student;
import com.ams.entity.User;
import com.ams.repositories.StudentRepository;
import com.ams.service.StudentService;
import com.ams.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StudentIntegrationTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    private static final String TEST_STUDENT_ID = "STU101";
    private static final String RAW_PASSWORD = "password123";

    private Student student;

    /** Remove any pre-existing student before each test and setup fresh one. */
    @BeforeEach
    void setup() {
        studentRepository.findByStudentId(TEST_STUDENT_ID).ifPresent(studentRepository::delete);
        createTestStudent();
    }

    /** Cleanup test data after each test run. */
    @AfterEach
    void tearDown() {
        studentRepository.findByStudentId(TEST_STUDENT_ID).ifPresent(studentRepository::delete);
    }

    /** Helper to create a test student and persist via service. */
    private void createTestStudent() {
        Contact contact = new Contact();
        contact.setMobileNo("9876543210");
        contact.setEmailId("john.doe@example.com");

        Student s = new Student();
        s.setStudentId(TEST_STUDENT_ID);
        s.setFullName("John Doe");
        s.setDepartment("Computer Science");
        s.setPickupLoc("Campus Gate 1");
        s.setContact(contact);

        // Service handles user + QR + encoding
        this.student = studentService.createStudent(s, RAW_PASSWORD);
    }

    /** ✅ Test student creation, user, QR, and DB persistence. */
    @Test
    void testCreateFullStudent() {
        assertNotNull(student.getId(), "Student should have an ID");
        assertEquals(TEST_STUDENT_ID, student.getStudentId());
        assertEquals("John Doe", student.getFullName());

        // Contact checks
        assertNotNull(student.getContact());
        assertEquals("9876543210", student.getContact().getMobileNo());
        assertEquals("john.doe@example.com", student.getContact().getEmailId());

        // User & password encoding
        assertNotNull(student.getUser());
        assertEquals(User.Role.STUDENT, student.getUser().getRole());
        assertTrue(passwordEncoder.matches(RAW_PASSWORD, student.getUser().getPasswordHash()));

        // QR code check
        assertNotNull(student.getQrImageUrl(), "QR Image URL should be generated");

        // Database persistence verification
        Student fromDb = studentRepository.findByStudentId(TEST_STUDENT_ID).orElse(null);
        assertNotNull(fromDb, "Student must exist in DB");
        assertEquals(student.getQrImageUrl(), fromDb.getQrImageUrl());
    }

    /** ✅ Test login success with correct role. */
    @Test
    void testStudentLoginSuccess() throws Exception {
        String json = String.format("""
                {
                  "username": "%s",
                  "password": "%s",
                  "role": "STUDENT"
                }
                """, student.getStudentId(), RAW_PASSWORD);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.payload.studentId").value(student.getStudentId()));
    }

    /** ❌ Test login fails when role is incorrect. */
    @Test
    void testStudentLoginRoleMismatch() throws Exception {
        String json = String.format("""
                {
                  "username": "%s",
                  "password": "%s",
                  "role": "ADMIN"
                }
                """, student.getStudentId(), RAW_PASSWORD);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }
}
