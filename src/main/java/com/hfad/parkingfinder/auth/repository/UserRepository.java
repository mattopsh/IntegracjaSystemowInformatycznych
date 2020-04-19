package com.hfad.parkingfinder.auth.repository;

import com.hfad.parkingfinder.auth.model.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM user_entity WHERE email = :email)", nativeQuery = true)
    boolean emailExists(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE user_entity SET fb_token = :fbToken WHERE email = :email", nativeQuery = true)
    void updateFbToken(@Param("email") String email, @Param("fbToken") String fbToken);

    Optional<UserEntity> findByEmail(String email);
}