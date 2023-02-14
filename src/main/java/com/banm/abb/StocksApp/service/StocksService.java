package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.SellStocksRequestDto;
import com.banm.abb.StocksApp.dto.StocksAvailableDto;
import com.banm.abb.StocksApp.dto.StocksOwnedDto;
import com.banm.abb.StocksApp.dto.StocksPurchaseRequestDto;
import com.banm.abb.StocksApp.exception.InvalidTransactionRequestException;
import com.banm.abb.StocksApp.model.StocksAvailable;
import com.banm.abb.StocksApp.model.StocksPurchased;
import com.banm.abb.StocksApp.model.User;
import com.banm.abb.StocksApp.repository.StocksAvailableRepository;
import com.banm.abb.StocksApp.repository.StocksPurchasedRepository;
import com.banm.abb.StocksApp.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface StocksService {

    List<StocksAvailableDto> listAllStocks();

    String purchaseStocks(StocksPurchaseRequestDto request);

    List<StocksOwnedDto> listOwnedStocks();

    String sellStocks(SellStocksRequestDto request);
}
