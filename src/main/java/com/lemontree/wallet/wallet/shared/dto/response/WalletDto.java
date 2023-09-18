package com.lemontree.wallet.wallet.shared.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class WalletDto {
    private UUID id;
    private BigDecimal balance;
    private BigDecimal balanceLimit;
    private UUID userId;

    public UUID getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getBalanceLimit() {
        return balanceLimit;
    }

    public UUID getUserId() {
        return userId;
    }

    public WalletDto(UUID id, BigDecimal balance, BigDecimal balanceLimit, UUID userId) {
        this.id = id;
        this.balance = balance;
        this.balanceLimit = balanceLimit;
        this.userId = userId;
    }

    protected WalletDto() {
    }
}
