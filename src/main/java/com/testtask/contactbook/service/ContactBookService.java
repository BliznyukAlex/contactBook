package com.testtask.contactbook.service;

import com.testtask.contactbook.domain.Contact;
import com.testtask.contactbook.domain.User;
import com.testtask.contactbook.dto.ContactDto;
import com.testtask.contactbook.dto.UserDto;
import com.testtask.contactbook.exception.ContactNotFoundException;
import com.testtask.contactbook.exception.PermissionException;
import com.testtask.contactbook.exception.UserExistsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface ContactBookService extends UserDetailsService {
    boolean addUser(UserDto userDto) throws UserExistsException;

    User getUser(Long userId);

    boolean addContact(ContactDto contactDto, Long userId) throws PermissionException;

    Contact getContact(Long contactId, Long userId) throws ContactNotFoundException, PermissionException;

    List<ContactDto> getAllUserContacts(Long userId);

    List<ContactDto> getAllContactsForAdmin();

    boolean updateContact(Long contactId, ContactDto contactDto, Long userId) throws ContactNotFoundException, PermissionException;

    boolean removeContact(Long contactId, Long userId) throws ContactNotFoundException, PermissionException;

    UserDetails getLoggedInUser();
}
