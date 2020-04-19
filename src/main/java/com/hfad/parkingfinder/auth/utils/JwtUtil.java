package com.hfad.parkingfinder.auth.utils;

import com.hfad.parkingfinder.auth.model.dto.TokenResponseDto;
import com.hfad.parkingfinder.auth.model.dto.UserDto;
import com.hfad.parkingfinder.exceptions.InternalServerErrorException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.val;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Component
public class JwtUtil {

    public static final String USER_ID = "USER_ID";
    public static final String USER_TYPE = "USER_TYPE";
    static final String USER_ENABLED = "USER_ENABLED";
    static final String TOKEN_TYPE = "TOKEN_TYPE";
    private static final int TOKEN_EXPIRATION_IN_DAYS = 90;

    @Value("${keystore.name}")
    private String keystoreName;

    @Value("${keystore.password}")
    private String keystorePassword;

    public boolean isTokenValid(@NonNull String token) {
        try {
            val body = getClaims(token);
            return body.getExpiration().after(DateTime.now().toDate());
        } catch (JwtException | ClassCastException e) {
            return false;
        }
    }

    public TokenResponseDto generateToken(@NonNull UserDto user) {
        val claims = Jwts.claims();
        claims.put(USER_ID, user.getUserId());
        claims.put(USER_TYPE, user.getUserType().name());
        claims.put(USER_ENABLED, user.isEnabled());
        claims.setExpiration(DateTime.now().plusDays(TOKEN_EXPIRATION_IN_DAYS).toDate());

        return new TokenResponseDto(Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, readPrivateKey())
                .compact());
    }

    public Claims getClaims(@NonNull String token) {
        if (hasBearerPrefix(token)) {
            token = removeBearerPrefix(token);
        }
        return Jwts.parser()
                .setSigningKey(readPrivateKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean hasBearerPrefix(@NonNull String bearerToken) {
        return bearerToken.startsWith("Bearer ");
    }

    private String removeBearerPrefix(@NonNull String bearerToken) {
        return bearerToken.substring(7);
    }

    @Cacheable("privateKey")
    private Key readPrivateKey() {
        ClassPathResource keystoreResource = new ClassPathResource(keystoreName + ".jks");
        try {
            val keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(keystoreResource.getInputStream(), keystorePassword.toCharArray());
            return keystore.getKey(keystoreName, keystorePassword.toCharArray());
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
            throw new InternalServerErrorException();
        }
    }
}
