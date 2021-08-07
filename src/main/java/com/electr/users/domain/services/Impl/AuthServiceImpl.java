package com.electr.users.domain.services.Impl;

import com.electr.users.authentication.jwt.JWTConfig;
import com.electr.users.domain.dto.LoginDTO;
import com.electr.users.domain.dto.UsuarioDTO;
import com.electr.users.domain.models.Usuario;
import com.electr.users.domain.models.UsuarioPrincipal;
import com.electr.users.domain.repositories.UsuarioRepository;
import com.electr.users.domain.services.AuthService;
import com.electr.users.exceptions.AllException;
import com.electr.users.utils.UsuarioConverter;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
@Qualifier("UserDetailsService")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final JWTConfig jwtConfig;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsuarioConverter usuarioConverter;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public HttpHeaders addTokenInHeaders(UsuarioPrincipal usuarioPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", jwtConfig.generateJwtToken(usuarioPrincipal));
        return headers;
    }

    @Override
    public ResponseEntity<UsuarioDTO> login(LoginDTO loginDTO) {
        Usuario usuarioAuth = findByAuthDetailsLogin(loginDTO.getEmail());
        UsuarioPrincipal usuarioPrincipal = new UsuarioPrincipal(usuarioAuth);

        HttpHeaders jwtHeader = addTokenInHeaders(usuarioPrincipal);

        String token = Objects.requireNonNull(jwtHeader.get("Authorization")).get(0);
        usuarioAuth.setToken(token);

        return new ResponseEntity<>(usuarioConverter.toModelDTO(usuarioRepository.save(usuarioAuth)), jwtHeader, HttpStatus.OK);
    }

    @Override
    public UsuarioDTO findAuthByToken(String token){
        Usuario usuario = usuarioRepository.findUsuarioByEmail(token);

        if(usuario == null) {
            throw new AllException("Usuário com este token não encontrado");
        }

        return usuarioConverter.toModelDTO(usuario);
    }

    @Override
    public UsuarioDTO isTokenValid(String token) {
        Usuario authTokenExistent = usuarioRepository.findUsuarioByToken(token);

       log.info(String.valueOf(authTokenExistent));

        try{
            jwtConfig.isTokenValid(authTokenExistent.getEmail(), token);
            return usuarioConverter.toModelDTO(authTokenExistent);
        }catch(Exception e ){
            throw new AllException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public Usuario findByAuthDetailsLogin(String email) {
        Usuario authIsExist = usuarioRepository.findUsuarioByEmail(email);

        if(authIsExist == null) {
            throw new AllException("Auth dont exist, try again with other username", HttpStatus.NOT_FOUND);
        }

        return authIsExist;
    }

    @Override
    public UserDetails loadUserByUsername(String authCredentialsLogin) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findUsuarioByEmail(authCredentialsLogin);

        if (usuario == null) {
            throw new AllException("User not found by username: " + authCredentialsLogin, HttpStatus.NOT_FOUND);
        } else {
            usuarioRepository.save(usuario);

            return new UsuarioPrincipal(usuario);
        }
    }
}
