package com.lemontree.wallet.wallet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;


@Table(name = "wire_transfer_history")
@Entity
public class WireTransferHistory {
    @Column(name = "id", columnDefinition = "binary(16)")
    @Id
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

    public WireTransferHistory(UUID id, BigDecimal amount, UUID sender, UUID receiver) {
        this.id = id;
        this.amount = amount;
        this.sender = sender;
        this.receiver = receiver;
    }

    protected WireTransferHistory() {
    }

    public static WireTransferHistory of(BigDecimal amount, UUID sender, UUID receiver) {
        return new WireTransferHistory(UUID.randomUUID(), amount, sender, receiver);
    }
}
