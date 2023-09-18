package com.lemontree.wallet.wallet.application;

import com.lemontree.wallet.user.domain.User;
import com.lemontree.wallet.wallet.application.impl.WalletServiceImpl;
import com.lemontree.wallet.wallet.domain.Balance;
import com.lemontree.wallet.wallet.domain.BalanceLimit;
import com.lemontree.wallet.wallet.domain.Wallet;
import com.lemontree.wallet.wallet.domain.WalletRepository;
import com.lemontree.wallet.wallet.domain.WireTransferHistory;
import com.lemontree.wallet.wallet.domain.WireTransferHistoryRepository;
import com.lemontree.wallet.wallet.shared.dto.request.DepositMoneyRequest;
import com.lemontree.wallet.wallet.shared.dto.request.WireTransferRequest;
import com.lemontree.wallet.wallet.shared.dto.response.WalletDto;
import com.lemontree.wallet.wallet.shared.dto.response.WireTransferHistoryDto;
import com.lemontree.wallet.wallet.shared.exception.BalanceLimitExceedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.lemontree.wallet.fixture.UserFixtures.createUser;
import static com.lemontree.wallet.fixture.WalletFixtures.createWallet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {
    @Mock
    WalletRepository walletRepository;

    @Mock
    WireTransferHistoryRepository wireTransferHistoryRepository;

    @InjectMocks
    WalletServiceImpl walletService;

    User sender;
    User receiver;
    User depositUser;
    @BeforeEach
    void setUp() {
        sender = createUser();
        receiver = createUser();
        depositUser = createUser();
    }

    @DisplayName("다른 지갑으로 송금 성공")
    @Test
    void wireTransfer_to_other_wallet_success() {
        //given
        WireTransferRequest request = new WireTransferRequest(sender.getId(), BigDecimal.valueOf(10000), receiver.getId());
        Wallet senderWallet = createWallet(new Balance(BigDecimal.valueOf(40000)), new BalanceLimit(BigDecimal.valueOf(200000)), sender.getId());
        BigDecimal senderPreviousBalance = senderWallet.getBalance();
        Wallet receiverWallet = createWallet(new Balance(BigDecimal.valueOf(50000)), new BalanceLimit(BigDecimal.valueOf(100000)), sender.getId());
        BigDecimal receiverPreviousBalance = receiverWallet.getBalance();

        given(walletRepository.findByUserId(request.getSenderUserId()))
                .willReturn(Optional.of(senderWallet));
        given(walletRepository.findByUserId(request.getReceiverUserId()))
                .willReturn(Optional.of(receiverWallet));

        //when
        WalletDto senderWalletDto = walletService.wireTransfer(request);

        //then
        assertThat(senderWalletDto.getBalance()).isEqualTo(senderPreviousBalance.subtract(request.getWireTransferAmount()));
        assertThat(receiverWallet.getBalance()).isEqualTo(receiverPreviousBalance.add(request.getWireTransferAmount()));
    }

    @DisplayName("한도가 넘을 경우 해당 송금은 실패해야 합니다.")
    @Test
    void wireTransfer_to_other_wallet_failed_because_balance_limit_exceed() {
        //given
        WireTransferRequest exceedAmountRequest = new WireTransferRequest(sender.getId(), BigDecimal.valueOf(100000), receiver.getId());
        Wallet senderWallet = createWallet(new Balance(BigDecimal.valueOf(140000)), new BalanceLimit(BigDecimal.valueOf(200000)), sender.getId());
        Wallet receiverWallet = createWallet(new Balance(BigDecimal.valueOf(50000)), new BalanceLimit(BigDecimal.valueOf(100000)), sender.getId());

        given(walletRepository.findByUserId(exceedAmountRequest.getSenderUserId()))
                .willReturn(Optional.of(senderWallet));
        given(walletRepository.findByUserId(exceedAmountRequest.getReceiverUserId()))
                .willReturn(Optional.of(receiverWallet));

        //when & then
        assertThatThrownBy(() -> walletService.wireTransfer(exceedAmountRequest))
                .isInstanceOf(BalanceLimitExceedException.class)
                .hasMessageContaining("한도 금액을 초과할 수 없습니다.");
    }

    @DisplayName("금액 충전 성공")
    @Test
    void deposit_money_success() {
        //given
        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest(depositUser.getId(), BigDecimal.valueOf(10000));
        Wallet wallet = Wallet.of(new Balance(BigDecimal.valueOf(10000)), new BalanceLimit(BigDecimal.valueOf(100000)), depositMoneyRequest.getUserId());
        BigDecimal previousBalance = wallet.getBalance();

        given(walletRepository.findByUserId(depositMoneyRequest.getUserId()))
                .willReturn(Optional.of(wallet));

        //when
        WalletDto walletDto = walletService.depositMoney(depositMoneyRequest);

        //then
        assertThat(walletDto.getBalance()).isEqualTo(previousBalance.add(depositMoneyRequest.getDepositMoneyAmount()));
    }

    @DisplayName("한도금액을 초과하는 만큼은 금액 충전을 할 수 없다.")
    @Test
    void deposit_money_failed_because_balance_limit_exceed() {
        //given
        DepositMoneyRequest exceedAmountRequest = new DepositMoneyRequest(depositUser.getId(), BigDecimal.valueOf(100000));
        Wallet wallet = Wallet.of(new Balance(BigDecimal.valueOf(10000)), new BalanceLimit(BigDecimal.valueOf(100000)), exceedAmountRequest.getUserId());

        given(walletRepository.findByUserId(exceedAmountRequest.getUserId()))
                .willReturn(Optional.of(wallet));

        //when & then
        assertThatThrownBy(() -> walletService.depositMoney(exceedAmountRequest))
                .isInstanceOf(BalanceLimitExceedException.class)
                .hasMessageContaining("한도 금액을 초과할 수 없습니다.");
    }

    @DisplayName("특정 유저에 대한 송금 내역을 확인할 수 있다.")
    @Test
    void search_wire_transfer_history_success() {
        //given
        List<WireTransferHistory> wireTransferHistories = List.of(
                WireTransferHistory.of(BigDecimal.valueOf(10000), sender.getId(), receiver.getId()),
                WireTransferHistory.of(BigDecimal.valueOf(20000), sender.getId(), receiver.getId())
        );

        given(wireTransferHistoryRepository.findAllBySenderOrReceiver(any()))
                .willReturn(wireTransferHistories);

        //when
        List<WireTransferHistoryDto> result = walletService.findAllWireTransferHistoriesByUserId(sender.getId());

        //then
        assertThat(result.size()).isEqualTo(wireTransferHistories.size());
    }
}
