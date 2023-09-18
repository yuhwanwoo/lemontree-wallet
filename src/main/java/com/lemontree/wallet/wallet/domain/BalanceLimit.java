package com.lemontree.wallet.wallet.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class BalanceLimit {

    @Column(name = "balanceLimit", nullable = false)
    private BigDecimal balanceLimit;

    public BigDecimal getBalanceLimit() {
        return balanceLimit;
    }

    public BalanceLimit(BigDecimal balanceLimit) {
        validateBalanceLimit(balanceLimit);
        this.balanceLimit = balanceLimit;
    }

    protected BalanceLimit() {

    }

    private void validateBalanceLimit(BigDecimal balanceLimit) {
        if (Objects.isNull(balanceLimit)) {
            throw new IllegalArgumentException("한도 금액은 필수로 존재해야 합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BalanceLimit that = (BalanceLimit) o;
        return Objects.equals(balanceLimit, that.balanceLimit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balanceLimit);
    }
}
