package com.hfad.parkingfinder.auth.utils;

import com.hfad.parkingfinder.auth.model.UserEntity;
import com.hfad.parkingfinder.auth.model.dto.TokensResponseDto;
import com.hfad.parkingfinder.auth.model.dto.UserDto;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.hfad.parkingfinder.auth.utils.JwtUtil.TOKEN_TYPE;
import static com.hfad.parkingfinder.auth.utils.JwtUtil.USER_ENABLED;
import static com.hfad.parkingfinder.auth.utils.JwtUtil.USER_ID;
import static org.junit.Assert.*;

public class JwtUtilTest {

    private static JwtUtil jwtUtil = new JwtUtil();
    private UserEntity user1 = UserEntity
            .builder()
            .userId(1)
            .enabled(true)
            .build();
    private UserEntity user2 = UserEntity
            .builder()
            .build();

    @BeforeClass
    public static void initJwtUtil() {
        ReflectionTestUtils.setField(jwtUtil, "keystoreName", "parkingapp");
        ReflectionTestUtils.setField(jwtUtil, "keystorePassword", "jnUJmFDGj?s7NcYRLLtNYvchDXdNkT");
    }

    @Test
    public void generateTokensPair() {
        jwtUtil.generateTokensPair(new UserDto(user1));
        jwtUtil.generateTokensPair(new UserDto(user2));
    }

    @Test
    public void validateToken() {
        TokensResponseDto tokensResponseDto = jwtUtil.generateTokensPair(new UserDto(user1));
        assertTrue(jwtUtil.validateToken(tokensResponseDto.getAccessToken().substring(7), JwtUtil.TokenType.ACCESS));
        assertTrue(jwtUtil.validateToken(tokensResponseDto.getRefreshToken(), JwtUtil.TokenType.REFRESH));
        assertFalse(jwtUtil.validateToken("FakeToken", JwtUtil.TokenType.ACCESS));
        assertFalse(jwtUtil.validateToken("FakeToken", JwtUtil.TokenType.REFRESH));
    }

    @Test
    public void generateAccessToken1() {
        TokensResponseDto tokensResponseDto = jwtUtil.generateTokensPair(new UserDto(user1));
        assertFalse(jwtUtil.generateAccessToken(tokensResponseDto.getRefreshToken()).isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateAccessToken3() {
        jwtUtil.generateAccessToken("");
    }

    @Test(expected = MalformedJwtException.class)
    public void generateAccessToken4() {
        jwtUtil.generateAccessToken("Nxjaknjxs");
    }

    @Test(expected = MalformedJwtException.class)
    public void getClaims1() {
        jwtUtil.getClaims("Bearer aaa");
    }

    @Test(expected = MalformedJwtException.class)
    public void getClaims2() {
        jwtUtil.getClaims("Beareraaa");
    }

    @Test(expected = MalformedJwtException.class)
    public void getClaims3() {
        jwtUtil.getClaims("hjcsbdhjc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getClaims4() {
        jwtUtil.getClaims("");
    }

    @Test
    public void getClaims5() {
        TokensResponseDto tokensResponseDto = jwtUtil.generateTokensPair(new UserDto(user1));
        assertEquals(user1.getUserId(), jwtUtil.getClaims(tokensResponseDto.getAccessToken()).get(USER_ID));
        assertEquals(user1.isEnabled(), jwtUtil.getClaims(tokensResponseDto.getAccessToken()).get(USER_ENABLED));
        assertEquals(JwtUtil.TokenType.ACCESS, JwtUtil.TokenType.valueOf((String) jwtUtil.getClaims(tokensResponseDto.getAccessToken()).get(TOKEN_TYPE)));
        assertEquals(user1.getUserId(), jwtUtil.getClaims(tokensResponseDto.getRefreshToken()).get(USER_ID));
        assertEquals(user1.isEnabled(), jwtUtil.getClaims(tokensResponseDto.getRefreshToken()).get(USER_ENABLED));
        assertEquals(JwtUtil.TokenType.REFRESH, JwtUtil.TokenType.valueOf((String) jwtUtil.getClaims(tokensResponseDto.getRefreshToken()).get(TOKEN_TYPE)));
    }

    @Test
    public void hasBearerPrefix() {
        assertTrue(jwtUtil.hasBearerPrefix("Bearer "));
        assertFalse(jwtUtil.hasBearerPrefix("BearerVGHXVGS"));
        assertFalse(jwtUtil.hasBearerPrefix("BVGHXVGS"));
    }
}