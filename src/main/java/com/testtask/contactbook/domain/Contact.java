package com.testtask.contactbook.domain;

import com.testtask.contactbook.dto.ContactDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @NotNull(message = "field can't be empty")
    @Size(min = 1, message = "field can't be empty")
    @Column(name = "first_name")
    private String firstName;
    @NotNull(message = "field can't be empty")
    @Size(min = 1, message = "field can't be empty")
    @Column(name = "last_name")
    private String lastName;
    @Pattern(regexp = "^\\+\\d{2}\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$")
    @Column(name = "phone")
    private String phone;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    public Contact(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
    public Contact(ContactDto contactDto){
        this.firstName = contactDto.getFirstName();
        this.lastName = contactDto.getLastName();
        this.phone = contactDto.getPhone();
    }
}
