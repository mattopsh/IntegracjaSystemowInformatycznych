package com.hfad.parkingfinder.parking.service;

import com.hfad.parkingfinder.parking.model.ParkingState;
import com.hfad.parkingfinder.parking.model.dto.ParkingStateDto;
import com.hfad.parkingfinder.parking.repository.ParkingStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.concurrent.CompletableFuture;

@Service
public class ReportService {

    @Autowired
    private ParkingStateRepository parkingStateRepository;
    @Autowired
    private Scheduler scheduler;

    public Mono<Void> reportState(int userId, ParkingStateDto parkingStateDto) {
        if (parkingStateDto.getParkingNodeId() != null) {
            return saveParkingState(parkingStateDto.getParkingNodeId(), userId, parkingStateDto);
        } else {
            // todo: wyszukanie najbliższego parkingu i zapisanie stanu
            return Mono.empty();
        }
    }

    private Mono<Void> saveParkingState(Long parkingNodeId, int userId, ParkingStateDto parkingStateDto) {
        return asyncRunnable(() -> parkingStateRepository.save(ParkingState.builder()
                .parkingNodeId(parkingNodeId)
                .parkingState(parkingStateDto.getParkingState())
                .userId(userId)
                .build()));
    }

    private Mono<Void> asyncRunnable(Runnable runnable) {
        return Mono.fromCompletionStage(CompletableFuture.runAsync(runnable)).publishOn(scheduler);
    }
}
