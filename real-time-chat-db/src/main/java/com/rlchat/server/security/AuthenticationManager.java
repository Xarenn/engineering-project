package com.rlchat.server.security;

import com.rlchat.server.exceptions.BadRequest;
import com.rlchat.server.security.util.TokenProvider;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    public static final String AUTHORITIES_KEY = "AUTHORITIES";
    private final TokenProvider tokenProvider;

    @Override
    public Mono authenticate(Authentication authentication) {
        final String authToken = authentication.getCredentials().toString();
        String username;
        try {
            username = tokenProvider.getSubjectFromToken(authToken);
        } catch (Exception e) {
            username = null;
        }
        if (username != null && !tokenProvider.isTokenExpired(authToken)) {
            Claims claims = tokenProvider.getAllClaimsFromToken(authToken);
            List<String> roles = claims.get(AUTHORITIES_KEY, List.class);
            List authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return Mono.just(auth);
        } else if(tokenProvider.isTokenExpired(authToken)){
            return Mono.error(new BadRequest("Token is Expired"));
        } else {
            return Mono.error(new BadRequest("Token Invalid"));
        }
    }
}
