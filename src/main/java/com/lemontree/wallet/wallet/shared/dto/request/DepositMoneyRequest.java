package com.lemontree.wallet.wallet.shared.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositMoneyRequest {
    private UUID userId;
    private BigDecimal depositMoneyAmount;

    public UUID getUserId() {
        return userId;
    }

    public BigDecimal getDepositMoneyAmount() {
        return depositMoneyAmount;
    }

    public DepositMoneyRequest(UUID userId, BigDecimal depositMoneyAmount) {
        this.userId = userId;
        this.depositMoneyAmount = depositMoneyAmount;
    }
}
