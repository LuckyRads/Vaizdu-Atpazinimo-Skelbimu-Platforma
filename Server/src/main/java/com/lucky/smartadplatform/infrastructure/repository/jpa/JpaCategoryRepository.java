package com.lucky.smartadplatform.infrastructure.repository.jpa;

import com.lucky.smartadplatform.domain.repository.CategoryRepository;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCategoryRepository extends JpaRepository<JpaCategory, Long>, CategoryRepository<JpaCategory> {

}
