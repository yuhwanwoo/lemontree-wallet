package com.lemontree.wallet.wallet.shared.exception;

public class BalanceLimitExceedException extends RuntimeException {
    public BalanceLimitExceedException(String message) {
        super(message);
    }
}
