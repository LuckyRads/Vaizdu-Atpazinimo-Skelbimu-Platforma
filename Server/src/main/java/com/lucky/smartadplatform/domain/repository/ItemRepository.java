package com.lucky.smartadplatform.domain.repository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository<T> {

    List<T> findAll();

    List<T> findAllByCategoryId(long categoryId);

    Optional<T> findById(Long id);

    T save(T item);

    void deleteById(Long id);

    void delete(T item);

}
