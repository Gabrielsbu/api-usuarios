package com.electr.users.domain.repositories;

import com.electr.users.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findUsuarioByEmail(String email);

    Usuario findUsuarioByToken(String token);

}