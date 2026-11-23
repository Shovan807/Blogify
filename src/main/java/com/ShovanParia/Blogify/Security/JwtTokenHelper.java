package com.ShovanParia.Blogify.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenHelper {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Generate signing key
    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate JWT token
    public String generateToken(UserDetails userDetails) {

        Date now = new Date();

        Date expiryDate =
                new Date(now.getTime() + expiration);

        return Jwts.builder()

                .subject(userDetails.getUsername())

                .issuedAt(now)

                .expiration(expiryDate)

                .signWith(getSigningKey())

                .compact();
    }


    // Extract email from JWT
    public String getUsernameFromToken(String token) {

        Claims claims = Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

        return claims.getSubject();
    }

    // Validate token
    public boolean validateToken(
            String token,
            UserDetails userDetails) {

        String username = getUsernameFromToken(token);
        System.out.println(username);
        return username.equals(userDetails.getUsername());
    }
}