package com.hfad.parkingfinder.parking.repository;

import com.hfad.parkingfinder.parking.model.ParkingState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ParkingStateRepository extends CrudRepository<ParkingState, Integer> {
    @Query(value = "SELECT parking_state AS parkingState, user_id AS userId FROM parking_state " +
            "WHERE parking_node_id = :parkingNodeId AND creation_timestamp > :startDate", nativeQuery = true)
    List<ParkingStateAndTypeDto> findParkingStates(@Param("parkingNodeId") Long parkingNodeId, @Param("startDate") Timestamp startDate);

    @Query(value = "SELECT CASE WHEN P.recordsNumber = 0 THEN 0 ELSE P.parkingState END AS parkingState, P.recordsNumber " +
            "FROM (SELECT AVG(parking_state) AS parkingState, COUNT(*) AS recordsNumber FROM parking_state " +
            "WHERE parking_node_id = :parkingNodeId AND creation_timestamp BETWEEN :startDate AND :endDate) AS P", nativeQuery = true)
    ParkingAvgHistoricalStateDto findAvgHistoricalParkingState(@Param("parkingNodeId") Long parkingNodeId, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query(value = "SELECT EXTRACT(dow FROM creation_timestamp) AS dayOfWeek, EXTRACT(hour from creation_timestamp) AS hour, AVG(parking_state) AS parkingState " +
            "FROM parking_state WHERE parking_node_id = :parkingNodeId AND creation_timestamp > :startDate " +
            "GROUP BY EXTRACT(dow FROM creation_timestamp), EXTRACT(hour from creation_timestamp)", nativeQuery = true)
    List<ParkingHistoryPointDto> findParkingHistory(@Param("parkingNodeId") Long parkingNodeId, @Param("startDate") Timestamp startDate);

    interface ParkingStateAndTypeDto {
        Integer getParkingState();
        Integer getUserId();
    }

    interface ParkingAvgHistoricalStateDto {
        Integer getParkingState();
        Integer getRecordsNumber();
    }

    interface ParkingHistoryPointDto {
        Integer getDayOfWeek();
        Integer getHour();
        Integer getParkingState();
    }
}
