package com.electr.users.domain.services;

import com.electr.users.domain.dto.LoginDTO;
import com.electr.users.domain.dto.UsuarioDTO;
import com.electr.users.domain.models.Usuario;
import com.electr.users.domain.models.UsuarioPrincipal;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public interface AuthService {

    Usuario findByAuthDetailsLogin(String username);

    ResponseEntity<UsuarioDTO> login(@RequestBody LoginDTO loginDTO);

    UsuarioDTO isTokenValid(String token);

    UsuarioDTO findAuthByToken(String token);

    HttpHeaders addTokenInHeaders(UsuarioPrincipal usuarioPrincipal);
}
