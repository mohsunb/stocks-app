package com.banm.abb.StocksApp.exception;

public class InvalidDepositRequestException extends RuntimeException {
    public InvalidDepositRequestException(String message) {
        super(message);
    }
}
