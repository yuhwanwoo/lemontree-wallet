package com.lemontree.wallet.wallet.infra;

import com.lemontree.wallet.wallet.domain.Wallet;
import com.lemontree.wallet.wallet.domain.WalletRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

public interface JpaWalletRepository extends WalletRepository, JpaRepository<Wallet, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findByUserId(UUID userId);
}
