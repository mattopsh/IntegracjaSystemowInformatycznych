package com.hfad.parkingfinder.report.model;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Entity;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OtherInconsistencyReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportId;
    private String description;
    private int userId;
}
