package com.hfad.parkingfinder.auth.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    private String email;
    private String encryptedPassword;
    private String fbToken;
    @Column(insertable = false)
    private Timestamp creationTimestamp;
    private boolean enabled;
}
