package com.project.system.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {

    private SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    // Generate a JWT token with user ID
    public String generateToken(Authentication auth, Long userId) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        // 1 year in milliseconds: 365 days * 24 hours * 60 minutes * 60 seconds * 1000 milliseconds
        long oneYearInMillis = 365L * 24 * 60 * 60 * 1000;

        // Uncomment one of the following lines depending on the desired validity period

        // For 1 year validity
        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + oneYearInMillis)) // Token valid for 1 year
                .claim("username", auth.getName())
                .claim("authorities", roles)
                .claim("userId", userId) // Add userId to the token claims
                .signWith(key)
                .compact();


        return jwt;
    }

    // Extract username from JWT token
    public String getUsernameFromJwtToken(String jwt) {
        jwt = jwt.substring(7); // Remove "Bearer " prefix

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return String.valueOf(claims.get("username"));
    }

    // Extract userId from JWT token
    public Long getUserIdFromJwtToken(String jwt) {
        jwt = jwt.substring(7); // Remove "Bearer " prefix

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        return claims.get("userId", Long.class); // Extract userId from claims
    }

    // Populate authorities as a comma-separated string
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();

        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }

        return String.join(",", auths);
    }
}
