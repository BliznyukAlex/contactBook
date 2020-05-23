package com.testtask.contactbook.service;

import com.testtask.contactbook.domain.Contact;
import com.testtask.contactbook.domain.Role;
import com.testtask.contactbook.domain.User;
import com.testtask.contactbook.dto.ContactDto;
import com.testtask.contactbook.dto.UserDto;
import com.testtask.contactbook.enums.Roles;
import com.testtask.contactbook.exception.ContactNotFoundException;
import com.testtask.contactbook.exception.PermissionException;
import com.testtask.contactbook.exception.UserExistsException;
import com.testtask.contactbook.repositories.ContactRepository;
import com.testtask.contactbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactBookServiceImpl implements ContactBookService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public boolean addUser(UserDto userDto) throws UserExistsException {
        User newUser = new User(userDto);
        User userFromDB = userRepository.findByUserName(newUser.getUsername());
        if (userFromDB != null) {
            throw new UserExistsException();
        }
        newUser.setRoles(Collections.singleton(new Role(1L, Roles.ROLE_USER.name())));
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return true;
    }

    @Override
    public User getUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user;
    }

    @Override
    public boolean addContact(ContactDto contactDto, Long userId) throws PermissionException {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) throw new UsernameNotFoundException("user not found");
        if (user.isAdmin()) throw new PermissionException();
        Contact contact = new Contact(contactDto);
        contact.setUser(user);
        contactRepository.save(contact);
        List<Contact> contacts = user.getContacts();
        contacts.add(contact);
        user.setContacts(contacts);
        userRepository.save(user);
        return true;
    }

    @Override
    public Contact getContact(Long contactId, Long userId) throws ContactNotFoundException, PermissionException {
        Contact contact = contactRepository.findById(contactId).orElse(null);
        if (contact == null) throw new ContactNotFoundException();
        checkPermission(contact, userId);
        return contact;
    }

    private void checkPermission(Contact contact, Long userId) throws PermissionException {
        User currentUser = userRepository.findById(userId).orElse(null);
        if (contact.getUser().getId() != currentUser.getId() && !currentUser.isAdmin()) throw new PermissionException();
    }

    @Override
    public List<ContactDto> getAllUserContacts(Long userId) {
        List<ContactDto> contacts;
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        UserDto userDto = new UserDto(user);
        contacts = userDto.getContactsDto(contactRepository.findAllByUserId(userId));
        return contacts;
    }

    @Override
    public List<ContactDto> getAllContactsForAdmin() {
//        return contactRepository.findAll().stream().map(c -> new ContactDto(c)).collect(Collectors.toList());
        return contactRepository.findAll().stream().map(ContactDto::new).collect(Collectors.toList());
    }

    @Override
    public boolean updateContact(Long contactId, ContactDto contactDto, Long userId) throws ContactNotFoundException, PermissionException {
        Contact contact = contactRepository.findById(contactId).orElse(null);
        if (contact == null) throw new ContactNotFoundException();
        checkPermission(contact, userId);
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setPhone(contactDto.getPhone());
        contactRepository.save(contact);
        return true;
    }

    @Override
    public boolean removeContact(Long contactId, Long userId) throws ContactNotFoundException, PermissionException {
        Contact contact = contactRepository.findById(contactId).orElse(null);
        if (contact == null) throw new ContactNotFoundException();
        checkPermission(contact, userId);
        contactRepository.deleteById(contactId);
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public UserDetails getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        System.out.println(userName);
        User user = userRepository.findByUserName(userName);
        System.out.println(user);
        return user;
    }
}
