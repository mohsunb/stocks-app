package com.banm.abb.StocksApp.controller;

import com.banm.abb.StocksApp.client.FeignTestClient;
import com.banm.abb.StocksApp.dto.SellStocksRequestDto;
import com.banm.abb.StocksApp.dto.StocksAvailableDto;
import com.banm.abb.StocksApp.dto.StocksOwnedDto;
import com.banm.abb.StocksApp.dto.StocksPurchaseRequestDto;
import com.banm.abb.StocksApp.service.StocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StocksController {

    private final StocksService service;

    @Autowired
    private FeignTestClient ftc;

    @GetMapping("/market/list-all")
    public ResponseEntity<List<StocksAvailableDto>> listAllStocks() {
        return ResponseEntity.ok(service.listAllStocks());
    }

    @GetMapping("/market/list-all/test-feign")
    public ResponseEntity<List<StocksAvailableDto>> testFeignByListAllStocks() {
        return ResponseEntity.ok(ftc.getStocks());
    }

    @PostMapping("/market/purchase")
    public String purchaseStocks(@RequestBody StocksPurchaseRequestDto request) {
        return service.purchaseStocks(request);
    }

    @GetMapping("/owned/list")
    public ResponseEntity<List<StocksOwnedDto>> listOwnedStocks() {
        return ResponseEntity.ok(service.listOwnedStocks());
    }

    @PostMapping("/owned/sell")
    public ResponseEntity<String> sellStocks(@RequestBody SellStocksRequestDto request) {
        return ResponseEntity.ok(service.sellStocks(request));
    }
}
