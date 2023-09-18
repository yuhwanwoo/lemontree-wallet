package com.lemontree.wallet.fixture;

import com.lemontree.wallet.wallet.domain.Balance;
import com.lemontree.wallet.wallet.domain.BalanceLimit;
import com.lemontree.wallet.wallet.domain.Wallet;

import java.util.UUID;

public class WalletFixtures {

    public static Wallet createWallet(Balance balance, BalanceLimit balanceLimit, UUID userId) {
        return Wallet.of(balance, balanceLimit, userId);
    };
}
