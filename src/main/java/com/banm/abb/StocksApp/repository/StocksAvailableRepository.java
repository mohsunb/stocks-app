package com.banm.abb.StocksApp.repository;

import com.banm.abb.StocksApp.model.StocksAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StocksAvailableRepository extends JpaRepository<StocksAvailable, Long> {

    Optional<StocksAvailable> findStocksAvailableByName(String name);
    Optional<StocksAvailable> findStocksAvailableById(Long id);
}
