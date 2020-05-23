package com.testtask.contactbook.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testtask.contactbook.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/contact-list-after.sql", "/sql/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RegistrationControllerTest {

    @Autowired
    RegistrationController controller;

    @Autowired
    MockMvc mockMvc;

    @Test
    void addUser() throws Exception {
        UserDto userDto = new UserDto("testUser", "test");
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(userDto);
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json);
        this.mockMvc.perform(builder)
                .andExpect(MockMvcResultMatchers.status()
                        .is(201))
                .andExpect(MockMvcResultMatchers.content()
                        .string("user created"));
    }
}