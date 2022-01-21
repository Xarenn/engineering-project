package com.rlchat.server.security.util;

import com.rlchat.server.security.properties.ServerProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class TokenProvider {

    private final ServerProperties serverProperties;

    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getSubjectFromTokenChecked(String token) {
        return getClaimFromTokenCheckException(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Date getExpirationDateFromTokenChecked(String token) {
        return getClaimFromTokenCheckException(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public <T> T getClaimFromTokenCheckException(String token, Function<Claims, T> claimsResolver) {
        Claims claims;
        try {
            claims = getAllClaimsFromToken(token);
        }catch (ExpiredJwtException exc) {
            return null;
        }
        return claimsResolver.apply(claims);
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(serverProperties.getSigningKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean isTokenExpiredChecked(String token) {
        final Date expiration = getExpirationDateFromTokenChecked(token);
        if(expiration == null) {
            return true;
        }
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token, String subject) {
        final String username = getSubjectFromToken(token);
        return (
                username.equals(subject)
                        && !isTokenExpired(token));
    }

    public Boolean validateTokenChecked(String token, String subject) {
        final String username = getSubjectFromTokenChecked(token);
        return (
                username.equals(subject)
                        && !isTokenExpired(token));
    }
}
