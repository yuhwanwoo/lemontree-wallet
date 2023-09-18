package com.lemontree.wallet.wallet.domain;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    Optional<Wallet> findByUserId(UUID userId);
}
