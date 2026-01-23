package com.finanzmanager.finanzapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // ❗ DEV-Secret (für PROD später via ENV!)
    private static final String SECRET =
            "finanzapp-finanzapp-finanzapp-finanzapp";

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 🔐 Token erzeugen (30 Tage gültig)
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)
                )
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // 👤 Username extrahieren (wirft KEINE ungefangene Exception mehr)
    public String extractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            // Token ist abgelaufen → bewusst null
            return null;
        } catch (JwtException e) {
            // Token manipuliert / ungültig
            return null;
        }
    }

    // ✅ Extra Check (Best Practice)
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
