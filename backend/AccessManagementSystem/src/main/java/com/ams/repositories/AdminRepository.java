package com.ams.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ams.entity.Admin;
import com.ams.entity.User;
import com.ams.entity.User;

import java.util.Optional;
import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

	/** Find Admin by username (used in login or validation) */
	Optional<Admin> findByUsername(String username);

	/** Optional: find Admin by email or mobile (via Contact relation) */
	Optional<Admin> findByContact_EmailId(String emailId);

	Optional<Admin> findByContact_MobileNo(String mobileNo);

	/** Optional: get all active admins */
	List<Admin> findByStatus(Admin.Status status);

	/** Optional: get Admin By user */
	Optional<Admin> findByUser(User user);
}
