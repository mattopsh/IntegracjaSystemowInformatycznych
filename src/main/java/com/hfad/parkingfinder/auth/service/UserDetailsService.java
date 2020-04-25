package com.hfad.parkingfinder.auth.service;

import com.hfad.parkingfinder.exceptions.UnauthorizedExcpetion;
import com.hfad.parkingfinder.auth.model.UserEntity;
import com.hfad.parkingfinder.auth.model.dto.UserPictureResponseDto;
import com.hfad.parkingfinder.auth.repository.UserRepository;
import com.hfad.parkingfinder.exceptions.NotFoundException;
import com.hfad.parkingfinder.auth.model.UserType;
import com.hfad.parkingfinder.auth.model.dto.UserDetailsResponseDto;
import com.hfad.parkingfinder.auth.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.hfad.parkingfinder.auth.utils.JwtUtil.USER_ID;
import static com.hfad.parkingfinder.auth.utils.JwtUtil.USER_TYPE;

@Service
public class UserDetailsService {

    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    private Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    public UserPictureResponseDto getUserPicture(String bearerToken) {
        Claims claims = jwtUtil.getClaims(bearerToken);
        int userId = (int) claims.get(USER_ID);
        if (UserType.valueOf((String) claims.get(USER_TYPE)) == UserType.FB_USER) {
            return getFbData(userId, UserPictureResponseDto.class, "picture");
        } else {
            throw new NotFoundException("Not a fb user. Pictures are only available for fb users.");
        }
    }

    public UserDetailsResponseDto getUserDetails(String bearerToken) {
        Claims claims = jwtUtil.getClaims(bearerToken);
        int userId = (int) claims.get(USER_ID);
        if (UserType.valueOf((String) claims.get(USER_TYPE)) == UserType.FB_USER) {
            return getFbData(userId, UserDetailsResponseDto.class, "first_name", "last_name", "picture");
        } else {
            return getUserDetailsFromDb(userId);
        }
    }

    private <T> T getFbData(int userId, Class<T> type, String... fbData) {

        Optional<UserEntity> user;
        try {
            user = userRepository.findById(userId);
        } catch (IllegalArgumentException e) {
            logger.info(e.getMessage());
            throw new UnauthorizedExcpetion("User doesn't exist");
        }
        if (user.isPresent()) {
            String fbToken = user.get().getFbToken();
            Facebook facebook = new FacebookTemplate(fbToken);
            try {
                return facebook.fetchObject("me", type, fbData);
            } catch (Exception e) {
                logger.debug(e.getMessage());
                throw new UnauthorizedExcpetion("Invalid facebook token");
            }
        } else {
            throw new UnauthorizedExcpetion("Invalid token");
        }
    }

    private UserDetailsResponseDto getUserDetailsFromDb(int userId) {
        Optional<UserEntity> user;
        try {
            user = userRepository.findById(userId);
        } catch (IllegalArgumentException e) {
            logger.info(e.getMessage());
            throw new UnauthorizedExcpetion("User doesn't exist");
        }
        if (user.isPresent()) {
            String email = user.get().getEmail();
            String name = user.get().getEmail().substring(0, email.indexOf("@"));
            return UserDetailsResponseDto.builder().first_name(name).build();
        } else {
            throw new UnauthorizedExcpetion("Invalid token");
        }
    }
}
