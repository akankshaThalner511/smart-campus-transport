package com.ams.service.base;

import com.ams.entity.Contact;
import com.ams.entity.User;
import com.ams.service.ContactService;
import com.ams.service.UserService;

/**
 * Base abstract service for all person-like entities (Student, Driver, Admin).
 * Provides utility methods to handle User and Contact creation.
 */
public abstract class AbstractPersonService {

	protected final UserService userService;
	protected final ContactService contactService;

	/**
	 * Constructor for dependency injection.
	 *
	 * @param userService    User service
	 * @param contactService Contact service
	 */
	protected AbstractPersonService(UserService userService, ContactService contactService) {
		this.userService = userService;
		this.contactService = contactService;
	}

	/**
	 * Helper method to create a User with associated Contact (if provided).
	 *
	 * @param username    Username for login
	 * @param rawPassword Raw password
	 * @param role        User.Role enum
	 * @param contact     Contact entity (optional)
	 * @return Created User entity
	 */
	protected User createUserAndContact(String username, String rawPassword, User.Role role, Contact contact) {
		String mobile = (contact != null) ? contact.getMobileNo() : null;
		String email = (contact != null) ? contact.getEmailId() : null;
		return userService.createUserWithContact(username, rawPassword, role, mobile, email);
	}
}
