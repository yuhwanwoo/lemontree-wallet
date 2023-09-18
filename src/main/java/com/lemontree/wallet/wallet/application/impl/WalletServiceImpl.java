package com.lemontree.wallet.wallet.application.impl;

import com.lemontree.wallet.wallet.application.WalletService;
import com.lemontree.wallet.wallet.domain.Balance;
import com.lemontree.wallet.wallet.domain.Wallet;
import com.lemontree.wallet.wallet.domain.WalletRepository;
import com.lemontree.wallet.wallet.domain.WireTransferHistory;
import com.lemontree.wallet.wallet.domain.WireTransferHistoryRepository;
import com.lemontree.wallet.wallet.shared.dto.request.DepositMoneyRequest;
import com.lemontree.wallet.wallet.shared.dto.request.WireTransferRequest;
import com.lemontree.wallet.wallet.shared.dto.response.WalletDto;
import com.lemontree.wallet.wallet.shared.dto.response.WireTransferHistoryDto;
import com.lemontree.wallet.wallet.shared.exception.LockConflictException;
import com.lemontree.wallet.wallet.shared.utils.ConvertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PessimisticLockException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WireTransferHistoryRepository wireTransferHistoryRepository;

    public WalletServiceImpl(WalletRepository walletRepository, WireTransferHistoryRepository wireTransferHistoryRepository) {
        this.walletRepository = walletRepository;
        this.wireTransferHistoryRepository = wireTransferHistoryRepository;
    }

    @Transactional
    @Override
    public WalletDto wireTransfer(WireTransferRequest wireTransferRequest) {
        try {
            Wallet senderWallet = walletRepository.findByUserId(wireTransferRequest.getSenderUserId())
                    .orElseThrow(() -> new EntityNotFoundException("송금자의 지갑을 찾을 수 없습니다."));
            Wallet receiverWallet = walletRepository.findByUserId(wireTransferRequest.getReceiverUserId())
                    .orElseThrow(() -> new EntityNotFoundException("수취자의 지갑을 찾을 수 없습니다."));

            BigDecimal wireTransferAmount = wireTransferRequest.getWireTransferAmount();

            senderWallet.send(new Balance(wireTransferAmount));
            receiverWallet.transfer(new Balance(wireTransferAmount));

            wireTransferHistoryRepository.save(
                    WireTransferHistory.of(
                            wireTransferRequest.getWireTransferAmount(),
                            wireTransferRequest.getSenderUserId(),
                            wireTransferRequest.getReceiverUserId()
                    )
            );

            return ConvertUtil.convert(senderWallet, WalletDto.class);
        } catch (PessimisticLockException e) {
            throw new LockConflictException(String.format("현재 계좌에 동시 접근 중입니다. sender: %s,  receiver: %s", wireTransferRequest.getSenderUserId(), wireTransferRequest.getReceiverUserId()));
        }

    }

    @Transactional
    @Override
    public WalletDto depositMoney(DepositMoneyRequest depositMoneyRequest) {
        try {
            Wallet wallet = walletRepository.findByUserId(depositMoneyRequest.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("금액 충전을 하려는 지갑을 찾을 수 없습니다. userId: " + depositMoneyRequest.getUserId()));

            wallet.transfer(new Balance(depositMoneyRequest.getDepositMoneyAmount()));
            return ConvertUtil.convert(wallet, WalletDto.class);
        } catch (PessimisticLockException e) {
            throw new LockConflictException("현재 사용자가 지갑을 동시에 금액 충전 중입니다.");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<WireTransferHistoryDto> findAllWireTransferHistoriesByUserId(UUID userId) {
        return ConvertUtil.convertList(wireTransferHistoryRepository.findAllBySenderOrReceiver(userId), WireTransferHistoryDto.class);
    }
}
