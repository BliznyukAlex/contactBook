package com.testtask.contactbook.service;

import com.testtask.contactbook.domain.Contact;
import com.testtask.contactbook.domain.User;
import com.testtask.contactbook.dto.ContactDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface ContactBookService extends UserDetailsService {
    boolean addUser(User user);
    User getUser(Long userId);
    boolean addContact(ContactDto contactDto, Long userId);
    Contact getContact(Long contactId);
    List<ContactDto> getAllUserContacts(Long userId);
    List<ContactDto> getAllContactsForAdmin();
    boolean updateContact(Long contactId, ContactDto contactDto);
    boolean removeContact(Long contactId);
    UserDetails getLoggedInUser();
}
