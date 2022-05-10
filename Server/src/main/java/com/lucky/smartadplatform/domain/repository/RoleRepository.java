package com.lucky.smartadplatform.domain.repository;

import java.util.Optional;

import com.lucky.smartadplatform.domain.type.RoleType;

public interface RoleRepository<T> {

    Optional<T> findByName(RoleType name);

    T save(T role);

}
