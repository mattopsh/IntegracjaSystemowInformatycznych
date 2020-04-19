package com.hfad.parkingfinder.parking.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ParkingState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int parkingStateId;
    private Long parkingNodeId;
    @Column(insertable = false)
    private Timestamp creationTimestamp;
    private int parkingState;
    private int userId;
}
