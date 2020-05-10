package com.testtask.contactbook.repositories;

import com.testtask.contactbook.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findAllByUserId(Long userId);
}
