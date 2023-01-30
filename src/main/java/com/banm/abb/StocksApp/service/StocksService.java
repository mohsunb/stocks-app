package com.banm.abb.StocksApp.service;

import com.banm.abb.StocksApp.dto.SellStocksRequestDto;
import com.banm.abb.StocksApp.dto.StocksAvailableDto;
import com.banm.abb.StocksApp.dto.StocksOwnedDto;
import com.banm.abb.StocksApp.dto.StocksPurchaseRequestDto;
import com.banm.abb.StocksApp.entity.StocksAvailable;
import com.banm.abb.StocksApp.entity.StocksPurchased;
import com.banm.abb.StocksApp.entity.User;
import com.banm.abb.StocksApp.repository.StocksAvailableRepository;
import com.banm.abb.StocksApp.repository.StocksPurchasedRepository;
import com.banm.abb.StocksApp.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StocksService {

    private final StocksAvailableRepository stocksAvailableRepository;
    private final StocksPurchasedRepository stocksPurchasedRepository;
    private final UsersRepository usersRepository;

    public List<StocksAvailableDto> listAllStocks() {
        List<StocksAvailableDto> list = new ArrayList<>();
        for (StocksAvailable s : stocksAvailableRepository.findAll())
            list.add(StocksAvailableDto.builder()
                    .name(s.getName())
                    .price(s.getPrice())
                    .availableCount(s.getAvailableCount()).build());
        return list;
    }

    @Transactional // This annotation is required to update the database in real-time
    public String purchaseStocks(StocksPurchaseRequestDto request) {
        var item = stocksAvailableRepository.findStocksAvailableByName(request.getName()).orElseThrow();

        Long id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = usersRepository.findById(id).orElseThrow();

        if (item.getPrice().multiply(new BigDecimal(request.getCount())).compareTo(user.getBalance()) > 0 ||
            request.getCount() > item.getAvailableCount())
            return "Purchase was failed: Invalid request.";

        var purchase = StocksPurchased.builder()
                .itemId(item.getId())
                .ownerId(user.getId())
                .count(request.getCount())
                .build();

        stocksPurchasedRepository.save(purchase);

        item.setAvailableCount(item.getAvailableCount() - request.getCount());

        user.setBalance(user.getBalance().subtract(item.getPrice().multiply(new BigDecimal(request.getCount()))));

        return "Purchase successful. " + user.getName() + " " + user.getSurname() + " purchased " + request.getCount()
                + " units of " + request.getName() + " stocks, totaling $"
                + item.getPrice().multiply(new BigDecimal(request.getCount())) + ".";
    }

    public List<StocksOwnedDto> listOwnedStocks() {
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var purchases = new ArrayList<>(stocksPurchasedRepository.findStocksPurchasedByOwnerId(user.getId()).orElseThrow());

        Map<Long, Integer> map = new HashMap<>();

        List<StocksOwnedDto> owned = new ArrayList<>();

        // Native queries are difficult :(
        for (StocksPurchased s : purchases) {
            if (!map.containsKey(s.getItemId()))
                map.put(s.getItemId(), s.getCount());
            else
                map.put(s.getItemId(), map.get(s.getItemId()) + s.getCount());
        }

        for (Long l : map.keySet()) {
            if (map.get(l) > 0) {
                owned.add(StocksOwnedDto.builder()
                        .name(stocksAvailableRepository.findStocksAvailableById(l).orElseThrow().getName())
                        .count(map.get(l))
                        .totalValue(stocksAvailableRepository.findStocksAvailableById(l).orElseThrow().getPrice()
                                .multiply(new BigDecimal(map.get(l))))
                        .build());
            }
        }

        return owned;
    }

    @Transactional
    public String sellStocks(SellStocksRequestDto request) {
        var list = listOwnedStocks();
        boolean canSell = false;

        for (StocksOwnedDto s : list) {
            if (s.getName().equals(request.getName()) && request.getCount() <= s.getCount()) {
                canSell = true;
                break;
            }
        }

        if (!canSell)
            return "Invalid request.";

        Long ownerId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        StocksAvailable item = stocksAvailableRepository.findStocksAvailableByName(request.getName()).orElseThrow();

        stocksPurchasedRepository.save(
                StocksPurchased.builder()
                        .ownerId(ownerId)
                        .itemId(item.getId())
                        .count(-request.getCount())
                        .build());

        User user = usersRepository.findById(ownerId).orElseThrow();
        user.setBalance(user.getBalance().add(item.getPrice().multiply(new BigDecimal(request.getCount()))));

        var stock = stocksAvailableRepository.findStocksAvailableByName(request.getName()).orElseThrow();
        stock.setAvailableCount(stock.getAvailableCount() + request.getCount());

        return "Sale successful. " + user.getName() + " " + user.getSurname() +
                " just cashed-out " + request.getCount() + " units of " + request.getName() + " stocks, totaling $" +
                item.getPrice().multiply(new BigDecimal(request.getCount())) + ".";
    }
}
