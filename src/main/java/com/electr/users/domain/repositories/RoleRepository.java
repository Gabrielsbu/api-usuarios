package com.electr.users.domain.repositories;

import com.electr.users.domain.models.Role;
import com.electr.users.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {



}