package com.hfad.parkingfinder.report.repository;

import com.hfad.parkingfinder.report.model.NonexistentParkingReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NonexistentParkingReportRepository extends CrudRepository<NonexistentParkingReport, Integer> {
}
