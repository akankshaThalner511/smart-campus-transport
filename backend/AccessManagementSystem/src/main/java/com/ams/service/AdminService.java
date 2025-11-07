package com.ams.service;

import com.ams.entity.Admin;
import com.ams.entity.Contact;
import com.ams.entity.User;
import com.ams.repositories.AdminRepository;
import com.ams.service.base.AbstractPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class AdminService extends AbstractPersonService {

	private final AdminRepository adminRepository;

	public AdminService(UserService userService, ContactService contactService, AdminRepository adminRepository) {
		super(userService, contactService);
		this.adminRepository = adminRepository;
	}

	/**
	 * Creates a new Admin along with linked User and Contact.
	 *
	 * @param admin       Admin entity with username, fullName, optional contact
	 * @param rawPassword Password for the linked User
	 * @return Saved Admin entity
	 */
	@Transactional
	public Admin createAdmin(Admin admin, String rawPassword) {
		log.info("Creating admin: {}", admin.getUsername());

		// Step 1: Handle contact if provided
		Contact contact = null;
		if (admin.getContact() != null) {
			contact = contactService.createOrGetContact(admin.getContact().getMobileNo(),
					admin.getContact().getEmailId());
		}

		// Step 2: Create User entry
		User user = userService.createUser(admin.getUsername(), rawPassword, User.Role.ADMIN, contact);

		// Step 3: Link User & Contact with Admin
		admin.setUser(user);
		admin.setContact(contact);
		admin.setStatus(Admin.Status.ACTIVE);

		// Step 4: Save Admin
		Admin saved = adminRepository.save(admin);
		log.info("Admin created successfully: {} (ID: {})", saved.getUsername(), saved.getAdminId());
		return saved;
	}

	/**
	 * Finds an admin by username.
	 *
	 * @param username Admin username
	 * @return Admin entity
	 */
	public Admin getAdminByUsername(String username) {
		return adminRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Admin not found with username: " + username));
	}

	public Admin getAdminByUser(User user) {
		return adminRepository.findByUser(user)
				.orElseThrow(() -> new RuntimeException("Admin not found with username: " + user.getUsername()));

	}
}
