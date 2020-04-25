package com.hfad.parkingfinder.auth.controller;

import com.hfad.parkingfinder.auth.model.dto.UserPictureResponseDto;
import com.hfad.parkingfinder.auth.service.AuthorizationService;
import com.hfad.parkingfinder.auth.service.UserDetailsService;
import com.hfad.parkingfinder.auth.model.dto.UserDetailsResponseDto;
import com.hfad.parkingfinder.config.RestExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthorizationService authorizationService;

    @RequestMapping(path = "/secured/picture", method = RequestMethod.GET)
    public UserPictureResponseDto getUserPicture(@RequestHeader("Authorization") String bearerToken) {
        return userDetailsService.getUserPicture(bearerToken);
    }

    @RequestMapping(path = "/secured/userDetails", method = RequestMethod.GET)
    public UserDetailsResponseDto getUserDeatils(@RequestHeader("Authorization") String bearerToken) {
        return userDetailsService.getUserDetails(bearerToken);
    }

}
