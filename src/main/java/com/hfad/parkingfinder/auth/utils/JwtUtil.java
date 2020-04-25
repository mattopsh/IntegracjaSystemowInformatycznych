package com.hfad.parkingfinder.auth.utils;

import com.hfad.parkingfinder.exceptions.InternalServerErrorException;
import com.hfad.parkingfinder.auth.model.dto.TokensResponseDto;
import com.hfad.parkingfinder.auth.model.dto.UserDto;
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

// To generate java key store: (.jks):
// RSA:     keytool -genkey -alias parkingapp -keyalg RSA -keystore parkingapp.jks -keysize 2048
// HMac:    keytool -genseckey -alias parkingapp -keyalg HMacSHA256 -keystore parkingapp.jks -keysize 2048
@Component
public class JwtUtil {

    public enum TokenType {
        ACCESS,
        REFRESH
    }

    public static final String USER_ID = "USER_ID";
    public static final String USER_TYPE = "USER_TYPE";
    static final String USER_ENABLED = "USER_ENABLED";
    static final String TOKEN_TYPE = "TOKEN_TYPE";
    private static final int ACCESS_TOKEN_EXPIRATION_IN_HOURS = 1;
    private static final int REFRESH_TOKEN_EXPIRATION_IN_DAYS = 90;

    @Value("${keystore.name}")
    private String keystoreName;

    @Value("${keystore.password}")
    private String keystorePassword;

    public boolean validateToken(@NonNull String token, TokenType expectedTokenType) {
        try {
            val body = getClaims(token);
            return body.getExpiration().after(DateTime.now().toDate()) && TokenType.valueOf((String) body.get(TOKEN_TYPE)) == expectedTokenType;
        } catch (JwtException | ClassCastException e) {
            return false;
        }
    }

    public TokensResponseDto generateTokensPair(@NonNull UserDto user) {
        String refreshToken = generateRefreshToken(user);
        String accessToken = generateAccessToken(refreshToken);
        return new TokensResponseDto(refreshToken, accessToken);
    }

    public String generateAccessToken(@NonNull final String refreshToken) {
        val claims = getClaims(refreshToken);
        claims.put(USER_ID, claims.get(USER_ID));
        claims.put(USER_TYPE, claims.get(USER_TYPE));
        claims.put(USER_ENABLED, claims.get(USER_ENABLED));
        claims.put(TOKEN_TYPE, TokenType.ACCESS.name());
        claims.setExpiration(DateTime.now().plusHours(ACCESS_TOKEN_EXPIRATION_IN_HOURS).toDate());

        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, readPrivateKey())
                .compact();
    }

    private String generateRefreshToken(@NonNull final UserDto user) {
        val claims = Jwts.claims();
        claims.put(USER_ID, user.getUserId());
        claims.put(USER_TYPE, user.getUserType().name());
        claims.put(USER_ENABLED, user.isEnabled());
        claims.put(TOKEN_TYPE, TokenType.REFRESH.name());
        claims.setExpiration(DateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_IN_DAYS).toDate());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, readPrivateKey())
                .compact();
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
