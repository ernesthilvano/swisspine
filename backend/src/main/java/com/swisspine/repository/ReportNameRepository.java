package com.swisspine.repository;

import com.swisspine.entity.ReportName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportNameRepository extends JpaRepository<ReportName, Long> {
    Optional<ReportName> findByNameIgnoreCase(String name);

    List<ReportName> findAllByOrderByNameAsc();

    @Query("SELECT rn FROM ReportName rn WHERE rn.reportType.id = :typeId ORDER BY rn.name ASC")
    List<ReportName> findByReportTypeIdOrderByNameAsc(@Param("typeId") Long typeId);
}
