package com.electr.users.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.electr.users.domain.models.UsuarioPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTConfig {

    @Value("${jwt.secret}")
    private String secret;

    public String generateJwtToken(UsuarioPrincipal usuarioPrincipal) {

        String[] claims = getClaimsFromUser(usuarioPrincipal);

        String role = getRoleFromUser(usuarioPrincipal);

        return JWT.create().withIssuer("Bem vindo ao Electr+")
                .withAudience("Portal de controle financeiro")
                .withIssuedAt(new Date()).withSubject(usuarioPrincipal.getUsername())
                .withArrayClaim("Authorities", claims).withClaim("Role", role)
                .withExpiresAt(new Date(System.currentTimeMillis() + 90000))
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

    public String getSubject(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getSubject();
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
        return usuarioPrincipal.getUsuario().getRole();
    }
}
