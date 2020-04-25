package com.hfad.parkingfinder.report.repository;

import com.hfad.parkingfinder.report.model.OtherInconsistencyReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtherInconsistencyRepository extends CrudRepository<OtherInconsistencyReport, Integer> {
}
