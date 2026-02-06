package com.swisspine.repository;

import com.swisspine.entity.FundAlias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundAliasRepository extends JpaRepository<FundAlias, Long> {
    List<FundAlias> findByFundIdOrderByAliasNameAsc(Long fundId);
}
