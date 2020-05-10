package com.testtask.contactbook.service;

import com.testtask.contactbook.domain.Contact;
import com.testtask.contactbook.domain.Role;
import com.testtask.contactbook.domain.User;
import com.testtask.contactbook.dto.ContactDto;
import com.testtask.contactbook.dto.UserDto;
import com.testtask.contactbook.repositories.ContactRepository;
import com.testtask.contactbook.repositories.RoleRepository;
import com.testtask.contactbook.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactBookServiceImpl implements ContactBookService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ContactRepository contactRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public boolean addUser(User user) {
        User userFromDB = userRepository.findByUserName(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public User getUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        return user;
    }

    @Override
    public boolean addContact(ContactDto contactDto, Long userId) {
        User user =  userRepository.findById(userId).orElse(null);
        if(user==null) return false;
        Contact contact = new Contact(contactDto);
        contact.setUser(user);
        contactRepository.save(contact);
        List<Contact> contacts= user.getContacts();
        contacts.add(contact);
        user.setContacts(contacts);
        userRepository.save(user);
        return true;
    }

    @Override
    public Contact getContact(Long contactId) {
        Contact contact = contactRepository.findById(contactId).orElse(null);
        return contact;
    }

    @Override
    public List<ContactDto> getAllUserContacts(Long userId) {
        List<ContactDto> contacts;
        User user  = userRepository.findById(userId).orElse(null);
        if(user==null) return null;
        UserDto userDto = new UserDto(user);
        contacts = userDto.getContactsDto(contactRepository.findAllByUserId(userId));
        return contacts;
    }

    @Override
    public List<ContactDto> getAllContactsForAdmin() {
        return  contactRepository.findAll().stream().map(c -> new ContactDto(c)).collect(Collectors.toList());
    }

    @Override
    public boolean updateContact(Long contactId, ContactDto contactDto) {
        Contact contact = contactRepository.findById(contactId).orElse(null);
        if(contact==null) return false;
        contact.setFirstName(contactDto.getFirstName());
        contact.setLastName(contactDto.getLastName());
        contact.setPhone(contactDto.getPhone());
        contactRepository.save(contact);
        return true;
    }

    @Override
    public boolean removeContact(Long contactId) {
        if(contactRepository.findById(contactId)==null) return false;
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
    public UserDetails getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        System.out.println(userName);
        User user= userRepository.findByUserName(userName);
        System.out.println(user);
        return user;
    }
}
