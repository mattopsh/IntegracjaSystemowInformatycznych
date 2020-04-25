package com.hfad.parkingfinder.report.service;

import com.hfad.parkingfinder.geodata.MapClient;
import com.hfad.parkingfinder.parking.model.ParkingState;
import com.hfad.parkingfinder.parking.repository.ParkingStateRepository;
import com.hfad.parkingfinder.report.model.NewParkingReport;
import com.hfad.parkingfinder.report.model.NonexistentParkingReport;
import com.hfad.parkingfinder.report.model.OtherInconsistencyReport;
import com.hfad.parkingfinder.report.model.dto.NewParkingReportDto;
import com.hfad.parkingfinder.report.model.dto.OtherInconsistencyDto;
import com.hfad.parkingfinder.report.model.dto.ParkingStateDto;
import com.hfad.parkingfinder.report.repository.NewParkingReportRepository;
import com.hfad.parkingfinder.report.repository.NonexistentParkingReportRepository;
import com.hfad.parkingfinder.report.repository.OtherInconsistencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.concurrent.CompletableFuture;

@Service
public class ReportService {

    @Autowired
    private NewParkingReportRepository newParkingReportRepository;
    @Autowired
    private NonexistentParkingReportRepository nonexistentParkingReportRepository;
    @Autowired
    private OtherInconsistencyRepository otherInconsistencyRepository;
    @Autowired
    private ParkingStateRepository parkingStateRepository;
    @Autowired
    private MapClient mapClient;
    @Autowired
    private Scheduler scheduler;

    public Mono<Void> reportNewParking(int userId, NewParkingReportDto newParkingReportDto) {
        return asyncRunnable(() -> newParkingReportRepository.save(NewParkingReport.builder()
                .attitude(newParkingReportDto.getAttitude())
                .longitude(newParkingReportDto.getLongitude())
                .capacity(newParkingReportDto.getCapacity())
                .otherInformation(newParkingReportDto.getOtherInformation())
                .stayCost(newParkingReportDto.getStayCost())
                .userId(userId)
                .build()));
    }

    public Mono<Void> reportNonexistentParking(int userId, Long parkingNodeId) {
        return asyncRunnable(() -> nonexistentParkingReportRepository.save(NonexistentParkingReport.builder()
                .parkingNodeId(parkingNodeId)
                .userId(userId)
                .build()));
    }

    public Mono<Void> reportOtherInconsistency(int userId, OtherInconsistencyDto otherInconsistencyDto) {
        return asyncRunnable(() -> otherInconsistencyRepository.save(OtherInconsistencyReport.builder()
                .description(otherInconsistencyDto.getDescription())
                .userId(userId)
                .build()));
    }


    public Mono<Void> reportState(int userId, ParkingStateDto parkingStateDto) {
        if (parkingStateDto.getParkingNodeId() != null) {
            return saveParkingState(parkingStateDto.getParkingNodeId(), userId, parkingStateDto);
        } else {
            return mapClient.findNearestParkingId(parkingStateDto.getAttitude(), parkingStateDto.getLongitude())
                    .flatMap(parkingNodeId -> saveParkingState(parkingNodeId, userId, parkingStateDto))
                    .onErrorMap(err -> err);
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
