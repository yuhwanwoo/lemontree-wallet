package com.lemontree.wallet.wallet.infra;

import com.lemontree.wallet.wallet.domain.WireTransferHistory;
import com.lemontree.wallet.wallet.domain.WireTransferHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaWireTransferHistoryRepository extends WireTransferHistoryRepository, JpaRepository<WireTransferHistory, UUID> {
    @Query(value = "select wthr from WireTransferHistory wthr where wthr.sender = :userId or wthr.receiver = :userId")
    List<WireTransferHistory> findAllBySenderOrReceiver(UUID userId);

    @Override
    WireTransferHistory save(WireTransferHistory wireTransferHistory);
}
