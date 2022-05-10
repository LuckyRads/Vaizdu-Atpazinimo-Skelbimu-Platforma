package com.lucky.smartadplatform.domain.repository;

import java.util.Optional;

public interface UserRepository<T> {

    Optional<T> findByUsername(String username);

    Optional<T> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    T save(T user);

}
