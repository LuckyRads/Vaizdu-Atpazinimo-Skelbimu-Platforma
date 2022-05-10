package com.lucky.smartadplatform.infrastructure.repository.jpa;

import java.util.List;

import com.lucky.smartadplatform.domain.repository.ImageRepository;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaImageRepository extends JpaRepository<JpaImage, Long>, ImageRepository<JpaImage> {

    @Query(value = "SELECT * FROM images JOIN users ON users.id = images.owner_id " +
            "WHERE users.username = :username", nativeQuery = true)
    List<JpaImage> findUserImages(@Param("username") String username);

}
