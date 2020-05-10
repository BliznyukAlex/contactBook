package com.testtask.contactbook.controllers;

import com.testtask.contactbook.annotations.CurrentUser;
import com.testtask.contactbook.domain.Contact;
import com.testtask.contactbook.domain.User;
import com.testtask.contactbook.dto.ContactDto;
import com.testtask.contactbook.service.ContactBookService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.util.List;

@NoArgsConstructor
@RestController
public class ContactBookController {

    @Autowired
    ContactBookService contactBookService;

    @GetMapping("/")
    public String start(){
        return "started successful!";
    }
    @PostMapping(value = "/user/contacts")
    public ResponseEntity<String> addContact(@Valid @RequestBody ContactDto contactDto, @CurrentUser User user){

        if(!user.isUser()) throw  new ResponseStatusException(HttpStatus.FORBIDDEN, "admin can only update and delete contacts");
        try {
            contactBookService.addContact(contactDto, user.getId());
        }
        catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough data", ex);
        }
        return new ResponseEntity<>("contact created", HttpStatus.CREATED);
    }

    @GetMapping("/user/contacts")
    public ResponseEntity<List<ContactDto>> getContacts(@CurrentUser User user) {
        try {
        if (user.isAdmin())
            return ResponseEntity.ok(contactBookService.getAllContactsForAdmin());
         else
            return ResponseEntity.ok(contactBookService.getAllUserContacts(user.getId()));
        } catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", ex);
        }
    }

    @GetMapping("/user/contacts/{contactId}")
    public ResponseEntity<ContactDto> getContact(@PathVariable Long contactId, @CurrentUser User user){
        Contact contact = contactBookService.getContact(contactId);
        try {
            return ResponseEntity.ok(new ContactDto(contact));
        } catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact not found", ex);
        }
    }

    @PutMapping("/user/contacts/{contactId}")
    public ResponseEntity<String> updateContact(@PathVariable Long contactId, @Valid @RequestBody ContactDto contactDto,@CurrentUser User user){
        Contact contact = contactBookService.getContact(contactId);
        if((contact.getUser().getId()!= user.getId())&&(!user.isAdmin()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "not enough rights");
        try {
            contactBookService.updateContact(contactId, contactDto);
        } catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not enough data", ex);
        }
        return ResponseEntity.ok("contact updated");
    }

    @DeleteMapping("/user/contacts/{contactId}")
    public ResponseEntity<String> removeContact(@PathVariable Long contactId, @CurrentUser User user){
        Contact contact = contactBookService.getContact(contactId);
        if((contact.getUser().getId()!= user.getId())&&(!user.isAdmin()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "not enough rights");
        try {
            contactBookService.removeContact(contactId);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "contact not found", ex);
        }
        return ResponseEntity.ok("contact deleted");
    }
}
