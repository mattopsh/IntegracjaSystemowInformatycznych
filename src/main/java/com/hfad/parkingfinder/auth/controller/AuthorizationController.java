package com.hfad.parkingfinder.auth.controller;


import com.hfad.parkingfinder.auth.model.dto.FbTokenDto;
import com.hfad.parkingfinder.auth.model.dto.TokenResponseDto;
import com.hfad.parkingfinder.auth.model.dto.UserCredentialsDto;
import com.hfad.parkingfinder.auth.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/public/v1/auth")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public TokenResponseDto logInEmail(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {
        return authorizationService.loginUser(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());
    }

    @RequestMapping(path = "/fblogin", method = RequestMethod.POST)
    public TokenResponseDto logInFB(@Valid @RequestBody FbTokenDto fbTokenDto) {
        return authorizationService.loginFbUser(fbTokenDto.getFbToken());
    }

    @RequestMapping(path = "/registration", method = RequestMethod.POST)
    public TokenResponseDto register(@Valid @RequestBody UserCredentialsDto userCredentialsDto) {
        return authorizationService.registerUser(userCredentialsDto.getEmail(), userCredentialsDto.getPassword());
    }
}