package com.electr.users.controllers;

import com.electr.users.domain.dto.LoginDTO;
import com.electr.users.domain.dto.UsuarioDTO;
import com.electr.users.domain.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authentication")
    public ResponseEntity<UsuarioDTO> signIn(@RequestBody LoginDTO loginDTO) {
        authenticate(loginDTO.getEmail(), loginDTO.getSenha());
        return authService.login(loginDTO);
    }

    public void authenticate(String email, String senha) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, senha));
    }
}
