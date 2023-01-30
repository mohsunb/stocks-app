package com.banm.abb.StocksApp.repository;

import com.banm.abb.StocksApp.entity.StocksPurchased;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StocksPurchasedRepository extends JpaRepository<StocksPurchased, Long> {
}
