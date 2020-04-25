package com.hfad.parkingfinder.report.model;

import com.hfad.parkingfinder.parking.model.Cost;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class NewParkingReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reportId;
    private Double attitude;
    private Double longitude;
    private Integer capacity;
    @Enumerated(EnumType.STRING)
    private Cost stayCost;
    private String otherInformation;
    private int userId;
}
