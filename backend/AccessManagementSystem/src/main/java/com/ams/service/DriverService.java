package com.ams.service;

import com.ams.entity.Contact;
import com.ams.entity.Driver;
import com.ams.entity.Student;
import com.ams.entity.User;
import com.ams.repositories.DriverRepository;
import com.ams.service.base.AbstractPersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@Service
public class DriverService extends AbstractPersonService {

	private final DriverRepository driverRepository;

	// Constructor for explicit super call
	public DriverService(UserService userService, ContactService contactService, DriverRepository driverRepository) {
		super(userService, contactService);
		this.driverRepository = driverRepository;
	}

	/**
	 * Create a new Driver along with linked User and Contact.
	 *
	 * @param driver      Driver entity with fullName, licenseNumber, contact info
	 * @param rawPassword Password for the linked User account
	 * @return Saved Driver entity
	 */
	@Transactional
	public Driver createDriver(Driver driver, String rawPassword) {
		log.info("Creating driver: {} - {}", driver.getLicenseNumber(), driver.getFullName());

		// Step 1: Create or get Contact (null-safe)
		Contact contact = Optional.ofNullable(driver.getContact())
				.map(c -> contactService.createOrGetContact(c.getMobileNo(), c.getEmailId())).orElse(null);

		// Step 2: Create User linked to this driver
		User user = userService.createUser(driver.getLicenseNumber(), rawPassword, User.Role.DRIVER, contact);

		// Step 3: Set user & contact to driver
		driver.setUser(user);
		driver.setContact(contact);

		// Step 4: Save driver
		Driver savedDriver = driverRepository.save(driver);
		log.info("Driver created successfully: {} (ID: {})", savedDriver.getFullName(), savedDriver.getDriverId());

		return savedDriver;
	}

	/**
	 * Find driver by license number.
	 *
	 * @param licenseNumber Driver license number
	 * @return Driver entity
	 */
	public Driver getDriverByLicenseNumber(String licenseNumber) {
		return driverRepository.findByLicenseNumber(licenseNumber)
				.orElseThrow(() -> new RuntimeException("Driver not found: " + licenseNumber));
	}

	/**
	 * Find student by linked User (used for JWT login payload)
	 *
	 * @param user The authenticated User
	 * @return Student entity linked to this User
	 */
	public Driver getDriverByUser(User user) {
		return driverRepository.findByUser(user)
				.orElseThrow(() -> new RuntimeException("Student not found for user: " + user.getUsername()));
	}

}
