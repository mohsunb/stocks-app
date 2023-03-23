package com.banm.abb.StocksApp.service.impl;

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
import com.banm.abb.StocksApp.service.EmailService;
import com.banm.abb.StocksApp.service.StocksService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StocksServiceImpl implements StocksService {

    private final StocksAvailableRepository stocksAvailableRepository;
    private final StocksPurchasedRepository stocksPurchasedRepository;
    private final UsersRepository usersRepository;
    private final EmailService emailService;

    public List<StocksAvailableDto> listAllStocks() {
        return stocksAvailableRepository.findAll().stream()
                .map(i -> StocksAvailableDto.builder()
                        .name(i.getName())
                        .price(i.getPrice())
                        .availableCount(i.getAvailableCount())
                        .build()).collect(Collectors.toList());
    }

    @Transactional
    public String purchaseStocks(StocksPurchaseRequestDto request) {
        var item = stocksAvailableRepository.findStocksAvailableByName(request.getName()).orElseThrow();

        Long id = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = usersRepository.findById(id).orElseThrow();

        if (item.getPrice().multiply(new BigDecimal(request.getCount())).compareTo(user.getBalance()) > 0 ||
                request.getCount() > item.getAvailableCount())
            throw new InvalidTransactionRequestException("Invalid request: Insufficient funds.");

        var purchase = StocksPurchased.builder()
                .itemId(item.getId())
                .ownerId(user.getId())
                .count(request.getCount())
                .build();

        stocksPurchasedRepository.save(purchase);

        item.setAvailableCount(item.getAvailableCount() - request.getCount());

        user.setBalance(user.getBalance().subtract(item.getPrice().multiply(new BigDecimal(request.getCount()))));

        String string = "Purchase successful. " + user.getName() + " " + user.getSurname() + " purchased " + request.getCount()
                + " units of " + request.getName() + " stocks, totaling $"
                + item.getPrice().multiply(new BigDecimal(request.getCount())) + ".\n\n" +
                "If you did not purchase stocks, someone else might have access to your account.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("Purchased stocks");
        message.setFrom("stocksappsmtpmohsun@gmail.com");
        message.setText(string);
        emailService.sendMail(message);

        return string;
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
            throw new InvalidTransactionRequestException("Invalid request: Cannot sell stocks not owned.");

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

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getUsername());
        message.setSubject("Stocks sold");
        message.setFrom("stocksappsmtpmohsun@gmail.com");
        String string = "Sale successful. " + user.getName() + " " + user.getSurname() +
                " just cashed-out " + request.getCount() + " units of " + request.getName() + " stocks, totaling $" +
                item.getPrice().multiply(new BigDecimal(request.getCount())) + ".\n\n" +
                "If you did not sell stocks, someone else might have access to your account.";
        message.setText(string);
        emailService.sendMail(message);

        return string;
    }
}
