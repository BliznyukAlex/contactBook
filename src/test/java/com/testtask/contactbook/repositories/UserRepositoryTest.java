package com.testtask.contactbook.repositories;

import com.testtask.contactbook.domain.Role;
import com.testtask.contactbook.domain.User;
import com.testtask.contactbook.enums.Roles;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import springfox.documentation.spring.web.plugins.DocumentationPluginsBootstrapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {
    @MockBean
    DocumentationPluginsBootstrapper mock;
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindAll() {
        User user = new User("username", "password");
        user.setRoles(Collections.singleton(new Role(1L, Roles.ROLE_USER.name())));
        userRepository.save(user);
        List<User> users = userRepository.findAll();
        assertEquals(users.size(), 1);
    }

    @Test
    @Sql(value = {"/sql/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findByUserName() {
        User userFromRepo = userRepository.findByUserName("user");
        assertTrue(userFromRepo.getUsername().equals("user"));
    }
}