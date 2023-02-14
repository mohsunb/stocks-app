package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.SellStocksRequestDto;
import com.banm.abb.StocksApp.dto.StocksAvailableDto;
import com.banm.abb.StocksApp.dto.StocksOwnedDto;
import com.banm.abb.StocksApp.dto.StocksPurchaseRequestDto;

import java.util.List;

public interface StocksService {

    List<StocksAvailableDto> listAllStocks();

    String purchaseStocks(StocksPurchaseRequestDto request);

    List<StocksOwnedDto> listOwnedStocks();

    String sellStocks(SellStocksRequestDto request);
}
