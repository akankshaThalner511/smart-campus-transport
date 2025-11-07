package com.ams.service;

import com.ams.entity.Contact;
import com.ams.repositories.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    /**
     * Creates or fetches a Contact entity by mobile number or email.
     * If the contact already exists, returns it.
     * Otherwise, creates and saves a new Contact record.
     *
     * @param mobileNo Mobile number (optional)
     * @param emailId  Email address (optional)
     * @return Existing or newly created Contact
     */
    @Transactional
    public Contact createOrGetContact(String mobileNo, String emailId) {

        // Normalize input
        mobileNo = (mobileNo != null && !mobileNo.isBlank()) ? mobileNo.trim() : null;
        emailId = (emailId != null && !emailId.isBlank()) ? emailId.trim().toLowerCase() : null;

        // If both missing, return null
        if (mobileNo == null && emailId == null) {
            log.warn("No contact info provided, returning null.");
            return null;
        }

        // Check if contact already exists
        Optional<Contact> existing = contactRepository.findByMobileNoOrEmailId(mobileNo, emailId);
        if (existing.isPresent()) {
            log.info("Found existing contact: {}", existing.get().getContactId());
            return existing.get();
        }

        // Create new Contact if not found
        Contact newContact = new Contact();
        newContact.setMobileNo(mobileNo);
        newContact.setEmailId(emailId);
        LocalDateTime now = LocalDateTime.now();
        newContact.setCreatedAt(now);
        newContact.setUpdatedAt(now);

        Contact savedContact = contactRepository.save(newContact);
        log.info("Created new contact: {} (Mobile: {}, Email: {})", savedContact.getContactId(), mobileNo, emailId);
        return savedContact;
    }

    /**
     * Finds a contact by email or mobile (optional utility method).
     *
     * @param mobileNo Mobile number
     * @param emailId  Email address
     * @return Optional Contact
     */
    public Optional<Contact> findByMobileOrEmail(String mobileNo, String emailId) {
        return contactRepository.findByMobileNoOrEmailId(mobileNo, emailId);
    }

    /**
     * Updates an existing contact if found.
     *
     * @param contact  Existing Contact entity
     * @param mobileNo New mobile number (optional)
     * @param emailId  New email address (optional)
     * @return Updated Contact entity
     */
    @Transactional
    public Contact updateContact(Contact contact, String mobileNo, String emailId) {
        if (contact == null) {
            log.warn("Attempted to update null contact.");
            return null;
        }

        if (mobileNo != null && !mobileNo.isBlank()) {
            contact.setMobileNo(mobileNo.trim());
        }
        if (emailId != null && !emailId.isBlank()) {
            contact.setEmailId(emailId.trim().toLowerCase());
        }
        contact.setUpdatedAt(LocalDateTime.now());

        Contact updated = contactRepository.save(contact);
        log.info("Updated contact: {} (Mobile: {}, Email: {})", updated.getContactId(), updated.getMobileNo(), updated.getEmailId());
        return updated;
    }
}
