package com.testtask.contactbook.controllers;

import com.testtask.contactbook.dto.UserDto;
import com.testtask.contactbook.exception.UserExistsException;
import com.testtask.contactbook.exception.ValidationException;
import com.testtask.contactbook.service.ContactBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RegistrationController {
    @Autowired
    ContactBookService contactBookService;

    @PostMapping("/registration")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserDto userDto) throws UserExistsException, ValidationException {
        contactBookService.addUser(userDto);
        return new ResponseEntity<>("user created", HttpStatus.CREATED);
    }
}
