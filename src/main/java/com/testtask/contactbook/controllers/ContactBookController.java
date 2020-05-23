package com.testtask.contactbook.controllers;

import com.testtask.contactbook.annotations.CurrentUser;
import com.testtask.contactbook.domain.Contact;
import com.testtask.contactbook.domain.User;
import com.testtask.contactbook.dto.ContactDto;
import com.testtask.contactbook.exception.ContactNotFoundException;
import com.testtask.contactbook.exception.PermissionException;
import com.testtask.contactbook.service.ContactBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@RestController
public class ContactBookController {

    @Autowired
    ContactBookService contactBookService;

    @GetMapping("/")
    public String start() {
        return "started successful!";
    }

    @PostMapping(value = "/user/contacts")
    public ResponseEntity<String> addContact(@Valid @RequestBody ContactDto contactDto,
                                             @CurrentUser User user) throws ValidationException, PermissionException {
        contactBookService.addContact(contactDto, user.getId());
        return new ResponseEntity<>("contact created", HttpStatus.CREATED);
    }

    @GetMapping("/user/contacts")
    public ResponseEntity<List<ContactDto>> getContacts(@CurrentUser User user) throws PermissionException {
        if (user.isAdmin())
            return ResponseEntity.ok(contactBookService.getAllContactsForAdmin());
        else
            return ResponseEntity.ok(contactBookService.getAllUserContacts(user.getId()));
    }

    @GetMapping("/user/contacts/{contactId}")
    public ResponseEntity<ContactDto> getContact(@PathVariable Long contactId,
                                                 @CurrentUser User user) throws ContactNotFoundException, PermissionException {
        Contact contact = contactBookService.getContact(contactId, user.getId());
        return ResponseEntity.ok(new ContactDto(contact));
    }

    @PutMapping("/user/contacts/{contactId}")
    public ResponseEntity<String> updateContact(@PathVariable Long contactId,
                                                @Valid @RequestBody ContactDto contactDto,
                                                @CurrentUser User user) throws ContactNotFoundException, PermissionException {
        contactBookService.updateContact(contactId, contactDto, user.getId());
        return ResponseEntity.ok("contact updated");
    }

    @DeleteMapping("/user/contacts/{contactId}")
    public ResponseEntity<String> removeContact(@PathVariable Long contactId,
                                                @CurrentUser User user) throws ContactNotFoundException, PermissionException {
        contactBookService.removeContact(contactId, user.getId());
        return ResponseEntity.ok("contact deleted");
    }
}
