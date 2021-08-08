package com.electr.users.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.electr.users.domain.dto.PayloadJwtDTO;
import com.electr.users.domain.dto.UsuarioDTO;
import com.electr.users.domain.models.Usuario;
import com.electr.users.domain.models.UsuarioPrincipal;
import com.electr.users.domain.repositories.UsuarioRepository;
import com.electr.users.exceptions.AllException;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JWTConfig {

    @Value("${jwt.secret}")
    private String secret;
    private final UsuarioRepository usuarioRepository;


    public String generateJwtToken(UsuarioPrincipal usuarioPrincipal) {

        String[] claims = getClaimsFromUser(usuarioPrincipal);

        String role = getRoleFromUser(usuarioPrincipal);

        return JWT.create().withIssuer("Bem vindo ao Electr+")
                .withAudience("Portal de controle financeiro")
                .withIssuedAt(new Date()).withSubject(usuarioPrincipal.getUsername())
                .withArrayClaim("Authorities", claims).withClaim("Role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + 900000))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    public List<GrantedAuthority> getAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(String nome, List<GrantedAuthority> authorities, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken userPasswordAuthToken = new
                UsernamePasswordAuthenticationToken(nome, null, authorities);
        userPasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return userPasswordAuthToken;
    }

    public JWTVerifier getJWTVerifier() {
        JWTVerifier verifier;

        try {
            Algorithm algorithm = Algorithm.HMAC512(secret);
            verifier = JWT.require(algorithm).withIssuer("Bem vindo ao Electr+").build();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("O token não foi verificado");
        }

        return verifier;
    }

    public boolean isTokenExpired(JWTVerifier verifier, String token) {
        long expiration = verifier.verify(token).getExpiresAt().getTime();
        return expiration >= new Date().getTime();
    }

    public boolean isTokenValid(String nome, String token) {
        JWTVerifier verifier = getJWTVerifier();
        return StringUtils.isNotEmpty(nome) && !isTokenExpired(verifier, token);
    }

    public String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim("Authorities").asArray(String.class);
    }

    private String[] getClaimsFromUser(UsuarioPrincipal usuarioPrincipal) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : usuarioPrincipal.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

    public String getRoleFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim("Role").asString();
    }

    private String getRoleFromUser(UsuarioPrincipal usuarioPrincipal) {
        return usuarioPrincipal.getUsuario().getRole().getRoleLabelPt();
    }

    public Usuario userExistent(String token){
        Usuario usuario = usuarioRepository.findUsuarioByToken(token);

        if(usuario == null) {
            throw new AllException("Autenticação não encontrada", HttpStatus.UNAUTHORIZED);
        }

        return usuario;
    }

    public PayloadJwtDTO getPayloadToken(String token){
        Base64.Decoder decoder = Base64.getDecoder();
        String[] partsBearer = token.split(" ");
        String[] partsToken = partsBearer[1].split("\\.");

        if (partsBearer.length != 2 || !"Bearer".equals(partsBearer[0])) {
            throw new AllException("Incorrect authorization structure");
        }

        Usuario usuario = userExistent(partsBearer[1]);

        if(usuario == null) {
            throw new AllException("Usuario não encontrado");
        }

        JWTVerifier verifier = getJWTVerifier();

        boolean tokenExpired = isTokenExpired(verifier, partsBearer[1]);

        if(!tokenExpired){
            throw new AllException("Token expirou", HttpStatus.UNAUTHORIZED);
        }

        String payload = new String(decoder.decode(partsToken[1]));

        Gson gson = new Gson();

        String payloadJson;
        payloadJson = payload.replace("Bearer ", "");
        return gson.fromJson(payloadJson, PayloadJwtDTO.class);
    }
}
