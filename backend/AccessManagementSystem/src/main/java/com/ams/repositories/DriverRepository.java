package com.ams.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ams.entity.Driver;
import com.ams.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {

	/** Find driver by unique driverId */
	Optional<Driver> findByDriverId(Integer driverId);

	/** Find driver by username (linked user) */
	Optional<Driver> findByUser_Username(String username);

	/** Find driver by status */
	List<Driver> findByStatus(Driver.Status status);

	/** Find driver by contact info */
	Optional<Driver> findByContact_EmailId(String emailId);

	Optional<Driver> findByContact_MobileNo(String mobileNo);

	/** Optional: filter drivers by license number */
	Optional<Driver> findByLicenseNumber(String licenseNumber);

	/** Optional: get driver by user */
	Optional<Driver> findByUser(User user);
}
