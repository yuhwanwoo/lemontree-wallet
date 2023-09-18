package com.lemontree.wallet.wallet.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Balance {
    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public Balance(BigDecimal balance) {
        validateBalance(balance);
        this.balance = balance;
    }

    protected Balance() {

    }

    private void validateBalance(BigDecimal balance) {
        if (Objects.isNull(balance)) {
            throw new IllegalArgumentException("잔액은 필수로 존재해야 합니다.");
        }
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("잔액은 0원 이상이어야 합니다.");
        }
    }
}
