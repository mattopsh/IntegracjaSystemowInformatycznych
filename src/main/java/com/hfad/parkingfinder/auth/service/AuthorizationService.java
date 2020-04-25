package com.hfad.parkingfinder.auth.service;

import com.hfad.parkingfinder.exceptions.ConflictException;
import com.hfad.parkingfinder.exceptions.UnauthorizedExcpetion;
import com.hfad.parkingfinder.auth.model.UserEntity;
import com.hfad.parkingfinder.auth.repository.UserRepository;
import com.hfad.parkingfinder.auth.model.dto.AccessTokenResponseDto;
import com.hfad.parkingfinder.auth.model.dto.FbDataResponseDto;
import com.hfad.parkingfinder.auth.model.dto.TokensResponseDto;
import com.hfad.parkingfinder.auth.model.dto.UserDto;
import com.hfad.parkingfinder.auth.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    private Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    public TokensResponseDto registerUser(String email, CharSequence password) {
        if (userRepository.emailExists(email)) {
            throw new ConflictException("There is already an account associated with this e-mail");
        }
        String encryptedPassword = passwordEncoder.encode(password);
        UserEntity user = UserEntity.builder()
                .email(email)
                .encryptedPassword(encryptedPassword)
                .enabled(true)
                .build();
        userRepository.save(user);
        return jwtUtil.generateTokensPair(new UserDto(user));
    }

    public TokensResponseDto loginUser(String email, CharSequence password) {
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getEncryptedPassword())) {
            return jwtUtil.generateTokensPair(new UserDto(user.get()));
        } else {
            throw new UnauthorizedExcpetion("Invalid email or password");
        }
    }

    public TokensResponseDto loginFbUser(String fbToken) {

        Facebook facebook = new FacebookTemplate(fbToken);
        FbDataResponseDto fbData;
        try {
            fbData = facebook.fetchObject("me", FbDataResponseDto.class, "email", "first_name", "last_name", "picture");
        } catch (Exception e) {
            logger.info(e.getMessage());
            throw new UnauthorizedExcpetion("Invalid facebook token");
        }
        UserEntity user = UserEntity.builder()
                .email(fbData.getEmail())
                .fbToken(fbToken)
                .enabled(true)
                .build();
        if (userRepository.emailExists(fbData.getEmail())) {
            userRepository.updateFbToken(fbData.getEmail(), fbToken);
        } else {
            userRepository.save(user);
        }
        return jwtUtil.generateTokensPair(new UserDto(user));
    }

    public void validateUser(String bearerToken) {
        if (!jwtUtil.hasBearerPrefix(bearerToken)) {
            throw new UnauthorizedExcpetion("No JWT token found in request headers");
        }
        boolean isTokenValid = jwtUtil.validateToken(bearerToken, JwtUtil.TokenType.ACCESS);
        if (!isTokenValid) {
            throw new UnauthorizedExcpetion("Invalid access token");
        }
    }

    public AccessTokenResponseDto generateAccessToken(String refreshToken) {
        boolean isTokenValid = jwtUtil.validateToken(refreshToken, JwtUtil.TokenType.REFRESH);
        if (isTokenValid) {
            return new AccessTokenResponseDto(jwtUtil.generateAccessToken(refreshToken));
        } else {
            throw new UnauthorizedExcpetion("Invalid refresh token");
        }
    }

}
