package com.testtask.contactbook.dto;

import com.testtask.contactbook.domain.Contact;
import com.testtask.contactbook.domain.Role;
import com.testtask.contactbook.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    private Set<RoleDto> roles;
    private List<ContactDto> contacts;

    public UserDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public UserDto(User user){
        this.id = user.getId();
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.roles = getRolesDto(user.getRoles());
        this.contacts = getContactsDto(user.getContacts());
    }
    public List<ContactDto> getContactsDto(List<Contact> contactsEntity){
        List<ContactDto> contactDtos = new ArrayList<>();
        for(Contact contact:contactsEntity){
            contactDtos.add(new ContactDto(contact));
        }
        return contactDtos;
    }
    private Set<RoleDto> getRolesDto(Set<Role> roleEntity){
        Set<RoleDto> roleDtos = new HashSet<>();
        for(Role role:roleEntity){
            roleDtos.add(new RoleDto(role));
        }
        return roleDtos;
    }
}
