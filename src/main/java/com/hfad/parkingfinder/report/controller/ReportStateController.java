package com.hfad.parkingfinder.report.controller;

import com.hfad.parkingfinder.auth.utils.JwtUtil;
import com.hfad.parkingfinder.exceptions.BadRequestException;
import com.hfad.parkingfinder.report.model.dto.ParkingStateDto;
import com.hfad.parkingfinder.report.model.dto.ParkingStateReportEnum;
import com.hfad.parkingfinder.report.service.ReportService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Objects;

import static com.hfad.parkingfinder.auth.utils.JwtUtil.USER_ID;

@RestController
@RequestMapping("api/v1/report/secured")
public class ReportStateController {

    @Autowired
    private ReportService reportService;
    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(path = "/state", method = RequestMethod.POST)
    public Mono<Void> reportState(@RequestHeader("Authorization") String bearerToken,
                                  @Valid @RequestBody ParkingStateDto parkingStateDto) {
        validateParkingState(parkingStateDto);
        validateLocationData(parkingStateDto);
        val userId = (int) jwtUtil.getClaims(bearerToken).get(USER_ID);
        return reportService.reportState(userId, parkingStateDto);
    }

    private void validateParkingState(ParkingStateDto parkingStateDto) {
        if (Arrays.stream(ParkingStateReportEnum.values()).noneMatch(parkingStateReportEnum -> Objects.equals(parkingStateReportEnum.getFreeSpaces(), parkingStateDto.getParkingState()))) {
            throw new BadRequestException("Invalid parking state in report");
        }
    }

    private void validateLocationData(ParkingStateDto parkingStateDto) {
        if (parkingStateDto.getParkingNodeId() == null && (parkingStateDto.getAttitude() == null || parkingStateDto.getLongitude() == null)) {
            throw new BadRequestException("Either parkingNodeId or attitude and longitude must be specified");
        }
    }

}
