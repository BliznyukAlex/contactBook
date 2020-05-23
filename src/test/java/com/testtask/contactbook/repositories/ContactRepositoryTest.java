package com.testtask.contactbook.repositories;

import com.testtask.contactbook.domain.Contact;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;
    @MockBean
    private UserRepository userRepository;

    @MockBean
    DocumentationPluginsBootstrapper mock;

    @Test
    void saveAndFindAll() {
        Contact contact = new Contact("first", "last", "+97(244)555-55-91");
        contactRepository.save(contact);
        List<Contact> contacts = contactRepository.findAll();
        Assert.assertEquals(contacts.size(), 1);
    }

    @Test
    @Sql(value = {"/sql/create-user-before.sql", "/sql/contact-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByUserId() {
        List<Contact> userContacts = contactRepository.findAllByUserId(1L);
        Assert.assertEquals(userContacts.size(), 2);
    }
}