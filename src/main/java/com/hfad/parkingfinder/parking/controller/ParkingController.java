package com.hfad.parkingfinder.parking.controller;

import com.hfad.parkingfinder.parking.model.dto.ParkingByLocationDto;
import com.hfad.parkingfinder.parking.repository.ParkingStateRepository;
import com.hfad.parkingfinder.parking.service.ParkingService;
import lombok.val;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/public/v1/parkings")
@Validated
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping
    public Flux<ParkingByLocationDto> findNearestParking(@RequestParam(defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(defaultValue = "20") @Min(1) Integer pageSize,
            @RequestParam @NotNull @Range(min = -90, max = 90) Double userLatitude,
            @RequestParam @NotNull @Range(min = -180, max = 180) Double userLongitude) {


        return parkingService.findParking(pageNumber, pageSize, userLatitude, userLongitude);
    }
}