package com.testtask.contactbook.domain;

import com.testtask.contactbook.dto.UserDto;
import com.testtask.contactbook.enums.Roles;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "user")
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "password")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Contact> contacts;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public User(UserDto userDto) {
        this.userName = userDto.getUserName();
        this.password = userDto.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean isAdmin() {
        Role adminRole = null;
        for (Role role : roles) {
            if (role.getName().equals(Roles.ROLE_ADMIN.name())) adminRole = role;
        }
        return adminRole != null;
    }

    public boolean isUser() {
        Role userRole = null;
        for (Role role : roles) {
            if (role.getName().equals(Roles.ROLE_USER.name())) userRole = role;
        }
        return userRole != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
