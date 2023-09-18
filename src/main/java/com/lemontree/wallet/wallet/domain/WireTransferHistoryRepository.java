package com.lemontree.wallet.wallet.domain;

import java.util.List;
import java.util.UUID;

public interface WireTransferHistoryRepository {
    List<WireTransferHistory> findAllBySenderOrReceiver(UUID userId);
    WireTransferHistory save(WireTransferHistory wireTransferHistory);
}
