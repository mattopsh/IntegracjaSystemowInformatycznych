package com.hfad.parkingfinder.parking.controller;

import com.hfad.parkingfinder.parking.model.Cost;
import com.hfad.parkingfinder.parking.model.DataVeracity;
import com.hfad.parkingfinder.parking.model.FreeSpaces;
import com.hfad.parkingfinder.parking.model.dto.FavoriteParkingStateDto;
import com.hfad.parkingfinder.parking.model.dto.ParkingByLocationDto;
import com.hfad.parkingfinder.parking.model.dto.ParkingNearToProviderDto;
import com.hfad.parkingfinder.parking.repository.ParkingStateRepository;
import com.hfad.parkingfinder.parking.service.ParkingService;
import lombok.val;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.hfad.parkingfinder.parking.service.ParkingService.NO_FILTER;

@RestController
@RequestMapping("/api/v1/parking")
@Validated
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @RequestMapping(path = "/public/nearToLocation", method = RequestMethod.GET)
    public Flux<ParkingByLocationDto> findNearestParking(
            @RequestParam(required = false) FreeSpaces freeSpaces,
            @RequestParam(required = false) DataVeracity dataVeracity,
            @RequestParam(required = false) Cost cost,
            @RequestParam(required = false) @Range(min = 100, max = 5000) Integer maxDistance,
            @RequestParam(required = false) String parkingNameOrAddress,
            @RequestParam(defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(defaultValue = "20") @Min(1) Integer pageSize,
            @RequestParam @NotNull @Range(min = -90, max = 90) Double attitude,
            @RequestParam @NotNull @Range(min = -180, max = 180) Double longitude) {

        val minFreeSpaces = getMinFreeSpaces(freeSpaces);
        val minDataVeracity = getMinDataVeracity(dataVeracity);
        val maxDist = getMaxDistance(maxDistance);
        return parkingService.findNearToLocation(pageNumber, pageSize, minFreeSpaces, minDataVeracity, cost, maxDist, parkingNameOrAddress, attitude, longitude);
    }

    @RequestMapping(path = "/public/nearToProvider", method = RequestMethod.GET)
    public Flux<ParkingNearToProviderDto> findNearToProvider(
            @RequestParam(required = false) FreeSpaces freeSpaces,
            @RequestParam(required = false) DataVeracity dataVeracity,
            @RequestParam(required = false) Cost cost,
            @RequestParam(required = false, defaultValue = "5000") @Range(min = 100, max = 5000) Integer maxDistance,
            @RequestParam(defaultValue = "0") @Min(0) Integer pageNumber,
            @RequestParam(defaultValue = "20") @Min(1) Integer pageSize,
            @RequestParam @NotNull @Range(min = -90, max = 90) Double userAttitude,
            @RequestParam @NotNull @Range(min = -180, max = 180) Double userLongitude,
            @RequestParam @NotNull @Range(min = -90, max = 90) Double providerAttitude,
            @RequestParam @NotNull @Range(min = -180, max = 180) Double providerLongitude) {

        val minFreeSpaces = getMinFreeSpaces(freeSpaces);
        val minDataVeracity = getMinDataVeracity(dataVeracity);
        return parkingService.findNearToProvider(pageNumber, pageSize, minFreeSpaces, minDataVeracity, cost, maxDistance, userAttitude, userLongitude, providerAttitude, providerLongitude);
    }

    @RequestMapping(path = "/public/favorite", method = RequestMethod.GET)
    public Flux<FavoriteParkingStateDto> getFavoriteParkingState(@RequestParam @NotNull List<Long> parkingNodesIds) {
        return parkingService.findFavoritesParkingStates(parkingNodesIds);
    }

    @RequestMapping(path = "/public/history", method = RequestMethod.GET)
    public Flux<ParkingStateRepository.ParkingHistoryPointDto> getParkingStatePrediction(@RequestParam @Min(1) Long parkingNodeId) {
        return parkingService.findParkingStatePrediction(parkingNodeId);
    }

    private int getMinFreeSpaces(FreeSpaces freeSpaces) {
        return freeSpaces != null ? freeSpaces.getMinFreeSpaces() : NO_FILTER;
    }

    private int getMinDataVeracity(DataVeracity dataVeracity) {
        return dataVeracity != null ? dataVeracity.getMinDataVeracity() : NO_FILTER;
    }

    private Integer getMaxDistance(Integer maxDistance){
        return maxDistance != null ? maxDistance : NO_FILTER;
    }
}