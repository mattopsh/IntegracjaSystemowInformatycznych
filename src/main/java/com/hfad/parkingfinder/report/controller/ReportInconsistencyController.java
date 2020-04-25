package com.hfad.parkingfinder.report.controller;

import com.hfad.parkingfinder.auth.utils.JwtUtil;
import com.hfad.parkingfinder.report.model.dto.NewParkingReportDto;
import com.hfad.parkingfinder.report.model.dto.OtherInconsistencyDto;
import com.hfad.parkingfinder.report.service.ReportService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static com.hfad.parkingfinder.auth.utils.JwtUtil.USER_ID;

@RestController
@RequestMapping("api/v1/inconsistency/secured")
@Validated
public class ReportInconsistencyController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ReportService reportService;

    @RequestMapping(path = "/new", method = RequestMethod.POST)
    public Mono<Void> reportNewParking(@RequestHeader("Authorization") String bearerToken,
                                       @Valid @RequestBody NewParkingReportDto newParkingReportDto) {
        val userId = (int) jwtUtil.getClaims(bearerToken).get(USER_ID);
        return reportService.reportNewParking(userId, newParkingReportDto);
    }
}
