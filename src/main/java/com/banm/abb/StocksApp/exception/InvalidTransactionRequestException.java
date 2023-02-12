package com.banm.abb.StocksApp.exception;

public class InvalidTransactionRequestException extends RuntimeException {
    public InvalidTransactionRequestException(String message) {
        super(message);
    }
}
