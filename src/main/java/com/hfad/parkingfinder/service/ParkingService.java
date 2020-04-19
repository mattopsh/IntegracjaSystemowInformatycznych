package com.hfad.parkingfinder.parking.service;

import com.hfad.parkingfinder.parking.model.dto.ParkingByLocationDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ParkingService {

    public Flux<ParkingByLocationDto> findParking(int pageNumber, int pageSize, double userLattitude, double userLongitude) {
        // todo: znalezienie parkingów w pobliżu użytkownika
        return Flux.empty();
    }

}