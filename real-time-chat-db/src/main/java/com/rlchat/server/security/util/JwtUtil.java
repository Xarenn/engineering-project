package com.rlchat.server.security.util;

import com.rlchat.server.domain.Role;
import com.rlchat.server.security.properties.ServerProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.rlchat.server.security.AuthenticationManager.AUTHORITIES_KEY;


@Component
@AllArgsConstructor
public class JwtUtil {

    private final ServerProperties serverProperties;

    public String getSecretForAuth() {
        return serverProperties.getSigningKey();
    }

    public String generateToken(String subject, List<Role> roles) {
        return createToken(subject, roles);
    }

    private String createToken(String id, List<Role> roles) {
        return Jwts.builder()
                .setSubject(id)
                .claim(AUTHORITIES_KEY, roles.stream().map(Enum::toString).collect(Collectors.toList()))
                .signWith(SignatureAlgorithm.HS512, serverProperties.getSigningKey())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + serverProperties.getAccessTokenValidity()*1000))
                .compact();
    }
}
