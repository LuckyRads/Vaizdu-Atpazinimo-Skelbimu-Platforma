package com.lucky.smartadplatform.infrastructure.repository.jpa;

import java.util.Optional;

import com.lucky.smartadplatform.domain.repository.UserRepository;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUser, Long>, UserRepository<JpaUser> {

	Optional<JpaUser> findByUsername(String username);

	Optional<JpaUser> findByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

}
