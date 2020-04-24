package com.hfad.parkingfinder.geodata;


import com.hfad.parkingfinder.geodata.osm.model.ProviderOsmType;
import com.hfad.parkingfinder.geodata.osm.model.dto.ParkingOsmDto;
import com.hfad.parkingfinder.geodata.osm.model.dto.ProviderOsmDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MapClient {

    int PARKING_RESPONSE_MAX_SIZE = 100;

    Mono<Long> findNearestParkingId(double attitude, double longitude);

    Flux<ParkingOsmDto> findParkingByNameOrAddress(double attitude, double longitude, String parkingNameOrAddress);

    Flux<ProviderOsmDto> findProviders(List<ProviderOsmType> providerOsmTypes, int maxDistance, String providerNameOrAddress, double attitude, double longitude);
}
