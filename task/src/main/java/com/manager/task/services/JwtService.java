package com.manager.task.services;
import java.util.function.Function;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails , Long expiration) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // email
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority()) // add role
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateAccessToken(UserDetails userDetails){
        final long expirationMillis = 1000 * 60 * 15; // 15 min
        return generateToken(userDetails, expirationMillis);
    }

    public String generateRefreshToken(UserDetails userDetails){
        final long expirationMillis = 1000L * 60 * 60 * 24 * 7; // 7 days
        return generateToken(userDetails, expirationMillis);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // remember we have writen .setSubject(userDetails.getUsername()) which is emailId in this case
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);  // here since username= email and email field is unique , there won't be 2 same user names
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // claims here indicate payload of a JWT
        return claimsResolver.apply(claims);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date()); // token is valid if expiry is in future
        } catch (JwtException | IllegalArgumentException e) {
            // JwtException covers: SignatureException, MalformedJwtException, ExpiredJwtException etc.
            return false;
        }
    }


}
