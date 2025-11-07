package com.ams.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ams.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	/** Find user by username (for login/authentication) */
	Optional<User> findByUsername(String username);

	/** Optional: find user by email (through contact) */
	Optional<User> findByContact_EmailId(String emailId);

	/** Optional: find user by mobile number (through contact) */
	Optional<User> findByContact_MobileNo(String mobileNo);

	/** Optional: check if username already exists */
	boolean existsByUsername(String username);
}
