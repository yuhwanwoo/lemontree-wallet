package com.lemontree.wallet.wallet.domain;

import com.lemontree.wallet.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.lemontree.wallet.fixture.UserFixtures.createUser;

public class WalletTest {

    User user;

    @BeforeEach
    void setUp() {
        user = createUser();
    }

    @DisplayName("지갑은 잔액을 필수로 가지고 있다.")
    @Test
    void wallet_must_contain_balance() {
        // when & then
        Assertions.assertThatThrownBy(() -> Wallet.of(new Balance(null), new BalanceLimit(BigDecimal.TEN), user.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잔액은 필수로 존재해야 합니다.");
    }

    @DisplayName("지갑은 잔액은 0원 이상이어야 한다.")
    @Test
    void wallet_balance_greater_than_zero() {
        // when & then
        Assertions.assertThatThrownBy(() -> Wallet.of(new Balance(BigDecimal.valueOf(-1)), new BalanceLimit(BigDecimal.TEN), user.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잔액은 0원 이상이어야 합니다.");
    }

    @DisplayName("지갑은 한도 금액을 필수로 가지고 있다.")
    @Test
    void wallet_must_contain_balance_limit() {
        // when & then
        Assertions.assertThatThrownBy(() -> Wallet.of(new Balance(BigDecimal.TEN), new BalanceLimit(null), user.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("한도 금액은 필수로 존재해야 합니다.");
    }
}
