package com.davisiqueira.fraud_guard.service.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.security.secret}")
    private String SECRET;

    @Value("${spring.security.issuer}")
    private String ISSUER;

    private Algorithm algorithm;

    @PostConstruct
    public void initService() {
        this.algorithm = Algorithm.HMAC256(SECRET);
    }

    public String generateToken(UserDetailsImpl user) throws JWTCreationException {
        return JWT.create()
                .withIssuer(ISSUER)
                .withIssuedAt(issuedAt())
                .withExpiresAt(expiresAt())
                .withSubject(user.getUsername())
                .sign(algorithm);
    }

    public String getSubjectFromToken(String token) throws JWTVerificationException {
        return JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .getSubject();
    }

    private Date issuedAt() {
        return toDate(LocalDateTime.now());
    }

    private Date expiresAt() {
        return toDate(LocalDateTime.now().plusHours(12));
    }

    private Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }


}
