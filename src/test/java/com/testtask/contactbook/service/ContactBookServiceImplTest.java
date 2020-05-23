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
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
class ContactBookServiceImplTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ContactRepository contactRepository;
    @Autowired
    private ContactBookServiceImpl contactBookService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static User user;
    private static UserDto userDto;
    private static Contact contact;
    private static ContactDto contactDto;
    private static List<Contact> contacts;

    @BeforeEach
    void initEach() {
        userDto = new UserDto("userName1", "123");
        user = new User(userDto);
        user.setRoles(Collections.singleton(new Role(1L, Roles.ROLE_USER.name())));
        user.setId(1L);
        contacts = new ArrayList<>();
        user.setContacts(contacts);
        contactDto = new ContactDto("firstName", "lastName", "+97(244)555-55-91");
        contact = new Contact(contactDto);
        contact.setUser(user);
        contact.setId(1L);
        contacts.add(contact);
    }

    @Test
    void addUser() throws UserExistsException {
        boolean isUserAdded = contactBookService.addUser(userDto);
        Assert.assertTrue(isUserAdded);
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));

    }

    @Test()
    void addUserFailed() {
        Mockito.doReturn(new User(userDto)).when(userRepository).findByUserName(userDto.getUserName());
        Assertions.assertThrows(UserExistsException.class, () -> contactBookService.addUser(userDto));
        Mockito.verify(userRepository, Mockito.times(0)).save(Mockito.any(User.class));
        Mockito.verify(userRepository, Mockito.times(1)).findByUserName(userDto.getUserName());
    }

    @Test
    void addContactByUser() throws PermissionException {
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        boolean isContactAdded = contactBookService.addContact(contactDto, 1L);
        Assert.assertTrue(isContactAdded);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(contactRepository, Mockito.times(1)).save(Mockito.any(Contact.class));
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void addContactByAdmin() {
        user.setRoles(Collections.singleton(new Role(1L, Roles.ROLE_ADMIN.name())));
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        Assertions.assertThrows(PermissionException.class, () -> contactBookService.addContact(contactDto, 1L));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void getContact() throws ContactNotFoundException, PermissionException {
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        Mockito.doReturn(Optional.of(contact)).when(contactRepository).findById(1L);
        Contact contactFromService = contactBookService.getContact(1L, 1L);
        Assert.assertEquals(contactFromService, contact);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(contactRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void contactNotFound() throws PermissionException {
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        Assertions.assertThrows(ContactNotFoundException.class, () -> contactBookService.getContact(1L, 1L));
        Mockito.verify(userRepository, Mockito.times(0)).findById(1L);
        Mockito.verify(contactRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void getAllUserContacts() {
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        Mockito.doReturn(contacts).when(contactRepository).findAllByUserId(1L);
        List<ContactDto> contactDtos = contactBookService.getAllUserContacts(1L);
        Assert.assertEquals(contactDtos.size(), 1);
        Assert.assertEquals(contactDtos.get(0), new ContactDto(contacts.get(0)));
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(contactRepository, Mockito.times(1)).findAllByUserId(1L);
    }

    @Test
    void getAllContactsForAdmin() {
        Mockito.doReturn(contacts).when(contactRepository).findAll();
        List<ContactDto> contactDtos = contactBookService.getAllContactsForAdmin();
        Assert.assertEquals(contactDtos.size(), 1);
        Assert.assertEquals(contactDtos.get(0), new ContactDto(contacts.get(0)));
        Mockito.verify(contactRepository, Mockito.times(1)).findAll();
    }

    @Test
    void updateContact() throws ContactNotFoundException, PermissionException {
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        Mockito.doReturn(Optional.of(contact)).when(contactRepository).findById(1L);
        ContactDto updateContactDto = new ContactDto("updateFirst", "updateLast", "+97(244)555-55-91");
        boolean isContactUpdate = contactBookService.updateContact(1L, updateContactDto, 1L);
        Assert.assertTrue(isContactUpdate);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(contactRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(contactRepository, Mockito.times(1)).save(Mockito.any(Contact.class));
    }

    @Test
    void removeContact() throws ContactNotFoundException, PermissionException {
        Mockito.doReturn(Optional.of(user)).when(userRepository).findById(1L);
        Mockito.doReturn(Optional.of(contact)).when(contactRepository).findById(1L);
        boolean isContactRemoved = contactBookService.removeContact(1L, 1L);
        Assert.assertTrue(isContactRemoved);
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(contactRepository, Mockito.times(1)).deleteById(1L);
    }
}