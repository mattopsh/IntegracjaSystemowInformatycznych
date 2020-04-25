package com.hfad.parkingfinder.parking.service;

import com.hfad.parkingfinder.geodata.MapClient;
import com.hfad.parkingfinder.geodata.distance.SimpleDistanceCalculator;
import com.hfad.parkingfinder.parking.model.Cost;
import com.hfad.parkingfinder.parking.model.dto.FavoriteParkingStateDto;
import com.hfad.parkingfinder.parking.model.dto.ParkingByLocationDto;
import com.hfad.parkingfinder.parking.model.dto.ParkingNearToProviderDto;
import com.hfad.parkingfinder.parking.model.dto.ParkingStateInfoDto;
import com.hfad.parkingfinder.parking.repository.ParkingStateRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

@Service
public class ParkingService {

    @Autowired
    private ParkingStateRepository parkingStateRepository;
    @Autowired
    private MapClient mapClient;
    @Autowired
    private Scheduler scheduler;
    @Value("${state.provider.user.id}")
    private Integer adminId;
    public final static int NO_FILTER = -1;

    public Flux<ParkingNearToProviderDto> findNearToProvider(int pageNumber, int pageSize, int minFreeSpaces, int minDataVeracity, Cost cost, int maxDistance, double userAttitude, double userLongitude, double providerAttitude, double providerLongitude) {
        return mapClient.findParkingByNameOrAddress(providerAttitude, providerLongitude, null)
                .map(parkingOsmDto -> Pair.of(parkingOsmDto, (int) SimpleDistanceCalculator.distance(userAttitude, userLongitude, parkingOsmDto.getAttitude(), parkingOsmDto.getLongitude())))
                .filter(parkingOsmDto -> cost == null || cost == Cost.fromOsmString(parkingOsmDto.getFirst().getParkingDetails().getFee()))
                .filter(pair -> maxDistance == NO_FILTER || pair.getSecond() <= maxDistance)
                .flatMap(parkingOsmDto ->
                        getParkingState(parkingOsmDto.getFirst().getParkingNodeId())
                                .defaultIfEmpty(ParkingStateInfoDto.builder()
                                        .parkingState(NO_FILTER)
                                        .dataVeracity(NO_FILTER)
                                        .build())
                                .zipWith(Mono.just(parkingOsmDto)))
                .map(parkingOsmDtoOrParkingState -> {
                    val parkingState = parkingOsmDtoOrParkingState.getT1();
                    val parkingOsmDto = parkingOsmDtoOrParkingState.getT2();
                    return ParkingNearToProviderDto.builder()
                            .parkingNodeId(parkingOsmDto.getFirst().getParkingNodeId())
                            .parkingName(parkingOsmDto.getFirst().getDisplayName())
                            .parkingAddress(parkingOsmDto.getFirst().getFullAddress())
                            .attitude(parkingOsmDto.getFirst().getAttitude())
                            .longitude(parkingOsmDto.getFirst().getLongitude())
                            .distanceToParking(parkingOsmDto.getSecond())
                            .distanceToProvider((int) SimpleDistanceCalculator.distance(parkingOsmDto.getFirst().getAttitude(), parkingOsmDto.getFirst().getLongitude(), providerAttitude, providerLongitude))
                            .freeSpaces(parkingState.getParkingState())
                            .dataVeracity(parkingState.getDataVeracity())
                            .build();
                })
                .filter(parkingNearToProvider -> parkingNearToProvider.getFreeSpaces() >= minFreeSpaces)
                .filter(parkingNearToProvider -> parkingNearToProvider.getDataVeracity() >= minDataVeracity)
                .sort(Comparator.comparingInt(parkingNearToProviderDto -> parkingNearToProviderDto.getDistanceToParking() * (1 - parkingNearToProviderDto.getDataVeracity())))
                .skip(pageNumber * pageSize)
                .take(pageSize);
    }


    public Flux<ParkingByLocationDto> findNearToLocation(int pageNumber, int pageSize, int minFreeSpaces, int minDataVeracity, Cost cost, int maxDistance, String parkingNameOrAddress, double userAttitude, double userLongitude) {
        return mapClient.findParkingByNameOrAddress(userAttitude, userLongitude, parkingNameOrAddress)
                .map(parkingOsmDto -> Pair.of(parkingOsmDto, (int) SimpleDistanceCalculator.distance(userAttitude, userLongitude, parkingOsmDto.getAttitude(), parkingOsmDto.getLongitude())))
                .filter(pair -> cost == null || cost == Cost.fromOsmString(pair.getFirst().getParkingDetails().getFee()))
                .filter(pair -> maxDistance == NO_FILTER || pair.getSecond() <= maxDistance)
                .flatMap(pair ->
                        getParkingState(pair.getFirst().getParkingNodeId())
                                .defaultIfEmpty(ParkingStateInfoDto.builder()
                                        .parkingState(NO_FILTER)
                                        .dataVeracity(NO_FILTER)
                                        .build())
                                .zipWith(Mono.just(pair)))
                .map(parkingOsmDtoAndParkingState -> {
                    val parkingState = parkingOsmDtoAndParkingState.getT1();
                    val parkingOsmDto = parkingOsmDtoAndParkingState.getT2();
                    return ParkingByLocationDto.builder()
                            .parkingNodeId(parkingOsmDto.getFirst().getParkingNodeId())
                            .parkingName(parkingOsmDto.getFirst().getDisplayName())
                            .parkingAddress(parkingOsmDto.getFirst().getFullAddress())
                            .attitude(parkingOsmDto.getFirst().getAttitude())
                            .longitude(parkingOsmDto.getFirst().getLongitude())
                            .distance(parkingOsmDto.getSecond())
                            .freeSpaces(parkingState.getParkingState())
                            .dataVeracity(parkingState.getDataVeracity())
                            .build();
                })
                .filter(parkingNearToProvider -> parkingNearToProvider.getFreeSpaces() >= minFreeSpaces)
                .filter(parkingNearToProvider -> parkingNearToProvider.getDataVeracity() >= minDataVeracity)
                .sort(Comparator.comparingInt(parkingNearToProviderDto -> parkingNearToProviderDto.getDistance() * (1 - parkingNearToProviderDto.getDataVeracity())))
                .skip(pageNumber * pageSize)
                .take(pageSize);
    }

    public Flux<FavoriteParkingStateDto> findFavoritesParkingStates(List<Long> favoriteParkingList) {
        return Flux.fromIterable(favoriteParkingList)
                .flatMap(parkingNodeId ->
                        getParkingState(parkingNodeId)
                                .defaultIfEmpty(ParkingStateInfoDto.builder()
                                        .parkingState(NO_FILTER)
                                        .dataVeracity(NO_FILTER)
                                        .build())
                                .zipWith(Mono.just(parkingNodeId)))
                .map(favoriteParkingNodeIdAndState -> {
                    val parkingState = favoriteParkingNodeIdAndState.getT1();
                    val parkingNodeId = favoriteParkingNodeIdAndState.getT2();
                    return FavoriteParkingStateDto.builder()
                            .parkingNodeId(parkingNodeId)
                            .parkingState(parkingState.getParkingState())
                            .dataVeracity(parkingState.getDataVeracity())
                            .build();
                });
    }

    public Flux<ParkingStateRepository.ParkingHistoryPointDto> findParkingStatePrediction(Long parkingNodeId) {
        return Flux.fromIterable(
                parkingStateRepository.findParkingHistory(parkingNodeId, Timestamp.valueOf(LocalDateTime.now().minusDays(7)))
        );
    }

    private Mono<ParkingStateInfoDto> getParkingState(Long parkingNodeId) {
        val nowMinus30 = Timestamp.valueOf(LocalDateTime.now().minusMinutes(30));
        val nowMinus60 = Timestamp.valueOf(LocalDateTime.now().minusMinutes(60));
        val lastWeekMinus60 = Timestamp.valueOf(LocalDateTime.now().minusWeeks(1).minusMinutes(60));
        val lastWeekPlus60 = Timestamp.valueOf(LocalDateTime.now().plusWeeks(1).plusMinutes(60));
        return computeParkingStateFromStateAndType(parkingStateRepository.findParkingStates(parkingNodeId, nowMinus30))
                .switchIfEmpty(computeParkingStateFromStateAndType(parkingStateRepository.findParkingStates(parkingNodeId, nowMinus60)))
                .switchIfEmpty(computeParkingStateFromAvgHistoricalState(parkingStateRepository.findAvgHistoricalParkingState(parkingNodeId, lastWeekMinus60, lastWeekPlus60)));
    }

    private Mono<ParkingStateInfoDto> computeParkingStateFromStateAndType(List<ParkingStateRepository.ParkingStateAndTypeDto> parkingStatesAndTypes) {
        if (parkingStatesAndTypes.stream().anyMatch(parkingStateAndTypeDto -> parkingStateAndTypeDto.getUserId().equals(adminId))) {
            return async(() -> ParkingStateInfoDto.builder()
                    .parkingState(parkingStatesAndTypes.get(parkingStatesAndTypes.size() - 1).getParkingState())
                    .dataVeracity(100)
                    .build());
        }
        if (!parkingStatesAndTypes.isEmpty()) {
            return async(() -> {
                val parkingState = parkingStatesAndTypes.stream().map(ParkingStateRepository.ParkingStateAndTypeDto::getParkingState).mapToDouble(state -> state).average();
                return ParkingStateInfoDto.builder()
                        .parkingState((int) parkingState.getAsDouble())
                        .dataVeracity(computeDataVeracity(parkingStatesAndTypes.size()))
                        .build();
            });
        } else {
            return Mono.empty();
        }
    }

    private Mono<ParkingStateInfoDto> computeParkingStateFromAvgHistoricalState(ParkingStateRepository.ParkingAvgHistoricalStateDto parkingAvgHistoricalState) {
        if (parkingAvgHistoricalState.getRecordsNumber() == 0) {
            return Mono.empty();
        } else {
            return async(() -> ParkingStateInfoDto.builder()
                    .parkingState(parkingAvgHistoricalState.getParkingState())
                    .dataVeracity(computeHistoricalDataVeracity(parkingAvgHistoricalState.getRecordsNumber()))
                    .build());
        }
    }

    private Integer computeDataVeracity(int numberOfRecords) {
        val dataVeracity = numberOfRecords * 10;
        if (dataVeracity > 100) {
            return 100;
        } else {
            return dataVeracity;
        }
    }

    private Integer computeHistoricalDataVeracity(int numberOfRecords) {
        val dataVeracity = numberOfRecords * 5;
        if (dataVeracity > 90) {
            return 90;
        } else {
            return dataVeracity;
        }
    }

    private <T> Mono<T> async(Callable<T> callable) {
        return Mono.fromCallable(callable).publishOn(scheduler);
    }

}