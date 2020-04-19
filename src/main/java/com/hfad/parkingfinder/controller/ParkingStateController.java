package com.hfad.parkingfinder.parking.controller;

import com.hfad.parkingfinder.auth.utils.JwtUtil;
import com.hfad.parkingfinder.parking.model.dto.ParkingStateDto;
import com.hfad.parkingfinder.parking.service.ReportService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

import static com.hfad.parkingfinder.auth.utils.JwtUtil.USER_ID;

@RestController
@RequestMapping("api/secured/v1/parking")
public class ParkingStateController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(path = "/state", method = RequestMethod.POST)
    public Mono<Void> reportState(@RequestHeader("Authorization") String bearerToken,
                                  @Valid @RequestBody ParkingStateDto parkingStateDto) {
        val userId = (int) jwtUtil.getClaims(bearerToken).get(USER_ID);
        return reportService.reportState(userId, parkingStateDto);
    }

}
