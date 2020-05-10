package com.testtask.contactbook.controllers;

import com.testtask.contactbook.domain.User;
import com.testtask.contactbook.dto.UserDto;
import com.testtask.contactbook.service.ContactBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class RegistrationController {
    @Autowired
    ContactBookService contactBookService;

    @PostMapping("/registration")
    public ResponseEntity<String> addUser(@RequestBody UserDto userDto){
        try {
            contactBookService.addUser(new User(userDto.getUserName(), userDto.getPassword()));
        }
        catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough data", ex);
        }
        return new ResponseEntity<>("user created", HttpStatus.CREATED);
    }
}
