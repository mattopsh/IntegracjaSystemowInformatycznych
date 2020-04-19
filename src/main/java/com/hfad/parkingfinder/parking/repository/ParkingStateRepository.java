package com.hfad.parkingfinder.parking.repository;

import com.hfad.parkingfinder.parking.model.ParkingState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingStateRepository extends CrudRepository<ParkingState, Integer> {
}
