package com.lemontree.wallet.wallet.shared.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class WireTransferHistoryDto {
    private UUID id;
    private BigDecimal amount;

    private UUID sender;
    private UUID receiver;

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public UUID getSender() {
        return sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public WireTransferHistoryDto(UUID id, BigDecimal amount, UUID sender, UUID receiver) {
        this.id = id;
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
    }

    protected WireTransferHistoryDto() {
    }
}
