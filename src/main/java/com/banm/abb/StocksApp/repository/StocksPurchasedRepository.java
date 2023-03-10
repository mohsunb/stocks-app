package com.banm.abb.StocksApp.repository;

import com.banm.abb.StocksApp.model.StocksPurchased;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StocksPurchasedRepository extends JpaRepository<StocksPurchased, Long> {

    Optional<List<StocksPurchased>> findStocksPurchasedByOwnerId(Long ownerId);
}
