package com.hfad.parkingfinder.auth.controller;


import com.hfad.parkingfinder.auth.model.dto.AccessTokenResponseDto;
import com.hfad.parkingfinder.auth.model.dto.FbTokenDto;
import com.hfad.parkingfinder.auth.model.dto.TokensResponseDto;
import com.hfad.parkingfinder.auth.model.dto.UserCredentialsDto;
import com.hfad.parkingfinder.auth.service.AuthorizationService;
import com.hfad.parkingfinder.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    @RequestMapping(path = "/public/login/email", method = RequestMethod.POST)
    public TokensResponseDto logInEmail(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {
        return authorizationService.loginUser(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());
    }

    @RequestMapping(path = "/public/login/fb", method = RequestMethod.POST)
    public TokensResponseDto logInFB(@Valid @RequestBody FbTokenDto fbTokenDto) {
        return authorizationService.loginFbUser(fbTokenDto.getFbToken());
    }

    @RequestMapping(path = "/public/registration", method = RequestMethod.POST)
    public TokensResponseDto register(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {
        return authorizationService.registerUser(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());
    }

    @RequestMapping(path = "/public/accessToken", method = RequestMethod.GET)
    public AccessTokenResponseDto generateAccessToken(@RequestHeader("refreshToken") String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new BadRequestException("Empty refresh token");
        }
        return authorizationService.generateAccessToken(refreshToken);
    }

}