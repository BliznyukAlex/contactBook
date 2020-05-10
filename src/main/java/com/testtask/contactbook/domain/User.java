package com.testtask.contactbook.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter @Setter
@Entity(name = "user")
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
    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
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
            if (role.getName().equals("ROLE_ADMIN")) adminRole = role;
        }
        return adminRole!=null;
    }
    public boolean isUser() {
        Role userRole = null;
        for (Role role : roles) {
            if (role.getName().equals("ROLE_USER")) userRole = role;
        }
        return userRole!=null;
    }
}
