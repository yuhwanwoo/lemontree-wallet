package com.lemontree.wallet.wallet.ui;

import com.lemontree.wallet.wallet.application.WalletService;
import com.lemontree.wallet.wallet.shared.dto.request.DepositMoneyRequest;
import com.lemontree.wallet.wallet.shared.dto.request.WireTransferRequest;
import com.lemontree.wallet.wallet.shared.dto.response.WalletDto;
import com.lemontree.wallet.wallet.shared.dto.response.WireTransferHistoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/api/v1/wallet/transfer")
    public ResponseEntity<WalletDto> wireTransfer(@RequestBody WireTransferRequest wireTransferRequest) {
        return ResponseEntity.ok(walletService.wireTransfer(wireTransferRequest));
    }

    @GetMapping("/api/v1/user/{userId}/wire-transfer-history")
    public ResponseEntity<List<WireTransferHistoryDto>> getWireTransferHistory(@PathVariable UUID userId) {
        return ResponseEntity.ok(walletService.findAllWireTransferHistoriesByUserId(userId));
    }

    @PostMapping("/api/v1/wallet/deposit")
    public ResponseEntity<WalletDto> depositMoney(@RequestBody DepositMoneyRequest depositMoneyRequest) {
        return ResponseEntity.ok(walletService.depositMoney(depositMoneyRequest));
    }
}
