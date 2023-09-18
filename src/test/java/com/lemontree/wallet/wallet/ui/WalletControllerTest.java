package com.lemontree.wallet.wallet.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemontree.wallet.user.domain.User;
import com.lemontree.wallet.wallet.application.WalletService;
import com.lemontree.wallet.wallet.shared.dto.request.DepositMoneyRequest;
import com.lemontree.wallet.wallet.shared.dto.request.WireTransferRequest;
import com.lemontree.wallet.wallet.shared.dto.response.WalletDto;
import com.lemontree.wallet.wallet.shared.dto.response.WireTransferHistoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.lemontree.wallet.fixture.UserFixtures.createUser;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(WalletController.class)
class WalletControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService walletService;

    User sender;
    User receiver;
    User depositUser;

    @BeforeEach
    void setUp() {
        sender = createUser();
        receiver = createUser();
        depositUser = createUser();
    }

    @DisplayName("송금 성공")
    @Test
    void wire_transfer_success() throws Exception {
        //given
        WireTransferRequest wireTransferRequest = new WireTransferRequest(sender.getId(), BigDecimal.TEN, receiver.getId());
        WalletDto walletDto = new WalletDto(UUID.fromString("625c6fc4-145d-408f-8dd5-33c16ba26064"), BigDecimal.TEN, BigDecimal.TEN, UUID.fromString("4721ee72-2ff3-417f-ade3-acd0a804605b"));

        given(walletService.wireTransfer(any()))
                .willReturn(walletDto);

        //when
        ResultActions result = mvc.perform(post("/api/v1/wallet/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wireTransferRequest)));

        //then
        result.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(walletDto.getId().toString()))
                .andExpect(jsonPath("$.balance").value(walletDto.getBalance()))
                .andExpect(jsonPath("$.balanceLimit").value(walletDto.getBalanceLimit()))
                .andExpect(jsonPath("$.userId").value(walletDto.getUserId().toString()));
    }

    @DisplayName("특정 유저의 송금 내역을 확인할 수 있다.")
    @Test
    void findAll_wireTransfer_history_by_userId_success() throws Exception {
        //given
        List<WireTransferHistoryDto> wireTransferHistoryDtos = Arrays.asList(new WireTransferHistoryDto(UUID.randomUUID(), BigDecimal.TEN, UUID.randomUUID(), UUID.randomUUID()));
        given(walletService.findAllWireTransferHistoriesByUserId(any()))
                .willReturn(wireTransferHistoryDtos);

        //when
        ResultActions result = mvc.perform(get("/api/v1/user/{userId}/wire-transfer-history", UUID.randomUUID()));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(wireTransferHistoryDtos.size())));
    }

    @DisplayName("지갑의 잔액을 충전할 수 있다.")
    @Test
    void deposit_money_success() throws Exception {
        //given
        DepositMoneyRequest depositMoneyRequest = new DepositMoneyRequest(UUID.randomUUID(), BigDecimal.TEN);
        WalletDto walletDto = new WalletDto(UUID.randomUUID(), depositMoneyRequest.getDepositMoneyAmount(), BigDecimal.valueOf(10000),depositMoneyRequest.getUserId());

        given(walletService.depositMoney(any()))
                .willReturn(walletDto);

        //when
        ResultActions result = mvc.perform(post("/api/v1/wallet/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositMoneyRequest)));

        //then
        result.andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(walletDto.getId().toString()))
                .andExpect(jsonPath("$.balance").value(walletDto.getBalance()))
                .andExpect(jsonPath("$.balanceLimit").value(walletDto.getBalanceLimit()))
                .andExpect(jsonPath("$.userId").value(walletDto.getUserId().toString()));
    }
}