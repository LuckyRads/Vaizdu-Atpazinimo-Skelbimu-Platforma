package com.lucky.smartadplatform.infrastructure.repository.jpa;

import com.lucky.smartadplatform.domain.repository.ItemRepository;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaItemRepository extends JpaRepository<JpaItem, Long>, ItemRepository<JpaItem> {

    @Query(value = "DELETE FROM items WHERE items.id = :id", nativeQuery = true)
    void deleteById(@Param("id") Long id);

}
