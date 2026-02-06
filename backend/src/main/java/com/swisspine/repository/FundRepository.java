package com.swisspine.repository;

import com.swisspine.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FundRepository extends JpaRepository<Fund, Long> {
    Optional<Fund> findByNameIgnoreCase(String name);

    List<Fund> findAllByOrderByNameAsc();
}
