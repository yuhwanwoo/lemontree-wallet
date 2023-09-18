package com.lemontree.wallet.wallet.shared.dto.request;

import java.math.BigDecimal;
import java.util.UUID;


public class WireTransferRequest {
    private UUID senderUserId;
    private BigDecimal wireTransferAmount;
    private UUID receiverUserId;

    public BigDecimal getWireTransferAmount() {
        return wireTransferAmount;
    }

    public UUID getSenderUserId() {
        return senderUserId;
    }

    public UUID getReceiverUserId() {
        return receiverUserId;
    }

    public WireTransferRequest(UUID senderUserId, BigDecimal wireTransferAmount, UUID receiverUserId) {
        this.senderUserId = senderUserId;
        this.wireTransferAmount = wireTransferAmount;
        this.receiverUserId = receiverUserId;
    }
}
