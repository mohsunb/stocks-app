package com.banm.abb.StocksApp.repository;

import com.banm.abb.StocksApp.entity.StocksAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksAvailableRepository extends JpaRepository<StocksAvailable, Long> {
}
