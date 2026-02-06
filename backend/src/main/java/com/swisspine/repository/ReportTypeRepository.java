package com.swisspine.repository;

import com.swisspine.entity.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {
    Optional<ReportType> findByNameIgnoreCase(String name);

    List<ReportType> findAllByOrderByNameAsc();
}
