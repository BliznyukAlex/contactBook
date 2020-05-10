package com.testtask.contactbook.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.testtask.contactbook.dto.ContactDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/contact-list-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/contact-list-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails(value = "user")
public class ContactBookControllerTest {

    @Autowired
    ContactBookController contactBookController;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void addContact() throws Exception {
        ContactDto contactDto = new ContactDto("Ole","Gunnar","+38(044)555-55-11");
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(contactDto);
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/user/contacts")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(json);
        this.mockMvc.perform(builder)
                .andExpect(authenticated())
                .andExpect(MockMvcResultMatchers.status()
                        .is(201))
                .andExpect(MockMvcResultMatchers.content()
                        .string("contact created"));
    }

    @Test
    public void getContacts() throws Exception {
        this.mockMvc.perform(get("/user/contacts"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("contact1FirstName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].lastName").value("contact1LastName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].phone").value("+38(044)555-55-33"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].firstName").value("contact2FirstName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].lastName").value("contact2LastName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].phone").value("+38(044)555-55-44"));
    }

    @Test
    public void getContact() throws Exception {
        this.mockMvc.perform(get("/user/contacts/1"))
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("contact1FirstName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("contact1LastName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value("+38(044)555-55-33"));
    }

    @Test
    public void updateContact() throws Exception {
        ContactDto contactDto = new ContactDto("Ole","Gunnar","+38(044)555-55-11");
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(contactDto);
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.put("/user/contacts/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(json);
        this.mockMvc.perform(builder)
                .andExpect(authenticated())
                .andExpect(MockMvcResultMatchers.status()
                .isOk())
                .andExpect(MockMvcResultMatchers.content()
                .string("contact updated"));

    }

    @Test
    public void removeContact() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/user/contacts/{contactId}",1))
                .andExpect(authenticated())
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("contact deleted"));
    }
}