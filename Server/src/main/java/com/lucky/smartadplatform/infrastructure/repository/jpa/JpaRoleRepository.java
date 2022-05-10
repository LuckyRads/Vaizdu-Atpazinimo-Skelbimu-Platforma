package com.lucky.smartadplatform.infrastructure.repository.jpa;

import java.util.Optional;

import com.lucky.smartadplatform.domain.repository.RoleRepository;
import com.lucky.smartadplatform.domain.type.RoleType;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaRole;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends JpaRepository<JpaRole, Long>, RoleRepository<JpaRole> {

    Optional<JpaRole> findByName(RoleType name);

}
