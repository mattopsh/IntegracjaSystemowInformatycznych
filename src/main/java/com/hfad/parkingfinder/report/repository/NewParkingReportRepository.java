package com.hfad.parkingfinder.report.repository;

import com.hfad.parkingfinder.report.model.NewParkingReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewParkingReportRepository extends CrudRepository<NewParkingReport, Integer> {
}
