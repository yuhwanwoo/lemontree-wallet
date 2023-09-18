package com.lemontree.wallet.wallet.domain;

import com.lemontree.wallet.wallet.shared.exception.BalanceLimitExceedException;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Table(name = "wallet")
@Entity
public class Wallet {

    @Column(name = "id", columnDefinition = "binary(16)")
    @Id
    private UUID id;

    @Embedded
    private Balance balance;

    @Embedded
    private BalanceLimit balanceLimit;

    private UUID userId;

    public Wallet(UUID id, Balance balance, BalanceLimit balanceLimit, UUID userId) {
        this.id = id;
        this.balance = balance;
        this.balanceLimit = balanceLimit;
        this.userId = userId;
    }

    protected Wallet() {

    }

    public static Wallet of(Balance balance, BalanceLimit balanceLimit, UUID userId) {
        return new Wallet(UUID.randomUUID(), balance, balanceLimit, userId);
    }

    public BigDecimal getBalance() {
        return balance.getBalance();
    }

    public BigDecimal getBalanceLimit() {
        return balanceLimit.getBalanceLimit();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void send(Balance wireTransferAmount) {
        this.balance = new Balance(balance.getBalance().subtract(wireTransferAmount.getBalance()));
    }

    private void validateBalanceLimit(Balance requestAmount) {
        if (balance.getBalance().add(requestAmount.getBalance()).compareTo(getBalanceLimit()) > 0) {
            throw new BalanceLimitExceedException("한도 금액을 초과할 수 없습니다.");
        }
    }

    public void transfer(Balance requestBalance) {
        validateBalanceLimit(requestBalance);
        this.balance = new Balance(balance.getBalance().add(requestBalance.getBalance()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
