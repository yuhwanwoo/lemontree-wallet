package com.lemontree.wallet.wallet.application;

import com.lemontree.wallet.wallet.domain.Wallet;
import com.lemontree.wallet.wallet.shared.dto.request.DepositMoneyRequest;
import com.lemontree.wallet.wallet.shared.dto.request.WireTransferRequest;
import com.lemontree.wallet.wallet.shared.dto.response.WalletDto;
import com.lemontree.wallet.wallet.shared.dto.response.WireTransferHistoryDto;

import java.util.List;
import java.util.UUID;

public interface WalletService {
    WalletDto wireTransfer(WireTransferRequest wireTransferRequest);

    WalletDto depositMoney(DepositMoneyRequest depositMoneyRequest);

    List<WireTransferHistoryDto> findAllWireTransferHistoriesByUserId(UUID userId);
}
