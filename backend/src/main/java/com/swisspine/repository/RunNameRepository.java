package com.swisspine.repository;

import com.swisspine.entity.RunName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RunNameRepository extends JpaRepository<RunName, Long> {
    Optional<RunName> findByNameIgnoreCase(String name);

    List<RunName> findAllByOrderByNameAsc();
}
