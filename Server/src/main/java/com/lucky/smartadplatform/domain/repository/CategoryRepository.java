package com.lucky.smartadplatform.domain.repository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository<T> {

    T save(T category);

    Optional<T> findById(Long id);

    Optional<T> findByName(String name);

    List<T> findAll();

    void deleteById(Long id);

}
