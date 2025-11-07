package com.ams.service;

import com.ams.entity.Contact;
import com.ams.entity.User;
import com.ams.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ContactService contactService;
    private final PasswordEncoder passwordEncoder; // Ensure PasswordEncoder bean is configured

    /**
     * Creates a User with the provided Contact entity.
     *
     * @param username    Username for login
     * @param rawPassword Raw password (will be encoded)
     * @param role        User role
     * @param contact     Linked Contact (optional)
     * @return Saved User entity
     */
    @Transactional
    public User createUser(String username, String rawPassword, User.Role role, Contact contact) {
        log.info("Creating user: {}", username);

        // Encode password safely
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(encodedPassword);
        user.setRole(role);
        user.setContact(contact);
        user.setStatus(User.AccountStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        log.info("User created successfully: {} (ID: {})", savedUser.getUsername(), savedUser.getUserId());
        return savedUser;
    }

    /**
     * Creates a User with mobile/email contact info.
     *
     * @param username    Username for login
     * @param rawPassword Raw password
     * @param role        User role
     * @param mobileNo    Mobile number (optional)
     * @param emailId     Email address (optional)
     * @return Saved User entity
     */
    @Transactional
    public User createUserWithContact(String username, String rawPassword, User.Role role, String mobileNo, String emailId) {
        Contact contact = contactService.createOrGetContact(mobileNo, emailId);
        return createUser(username, rawPassword, role, contact);
    }

    /**
     * Finds a user by username.
     *
     * @param username Username
     * @return User entity
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
