package com.swisspine.repository;

import com.swisspine.entity.SourceName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for SourceName master data.
 * 
 * @author SwissPine Engineering Team
 */
@Repository
public interface SourceNameRepository extends JpaRepository<SourceName, Long> {

    Optional<SourceName> findByNameIgnoreCase(String name);

    List<SourceName> findAllByOrderByNameAsc();
}
