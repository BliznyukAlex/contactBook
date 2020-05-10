package com.testtask.contactbook.dto;

import com.testtask.contactbook.domain.Contact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContactDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;

    public ContactDto(Contact contact){
        this.id = contact.getId();
        this.firstName = contact.getFirstName();
        this.lastName = contact.getLastName();
        this.phone = contact.getPhone();
    }

    public ContactDto(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
