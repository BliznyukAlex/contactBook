package com.testtask.contactbook.repositories;

import com.testtask.contactbook.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
