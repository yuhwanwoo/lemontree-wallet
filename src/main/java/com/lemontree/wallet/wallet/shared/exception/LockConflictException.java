package com.lemontree.wallet.wallet.shared.exception;

public class LockConflictException extends RuntimeException {
    public LockConflictException(String message) {
        super(message);
    }
}
