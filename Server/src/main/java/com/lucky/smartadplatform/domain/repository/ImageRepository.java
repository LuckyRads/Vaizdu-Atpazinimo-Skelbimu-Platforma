package com.lucky.smartadplatform.domain.repository;

import java.util.List;
import java.util.Optional;

public interface ImageRepository<T> {

    T save(T image);

    Optional<T> findById(Long id);

    List<T> findUserImages(String username);

    void deleteById(Long id);

}
