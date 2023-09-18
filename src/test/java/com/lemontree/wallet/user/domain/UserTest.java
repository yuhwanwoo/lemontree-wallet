package com.lemontree.wallet.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @DisplayName("유저는 가입상태 일 수 있다.")
    @Test
    void User_can_be_active_status() {
        //when & when
        assertThatThrownBy(() -> new User(UUID.randomUUID(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유저의 상태는 필수로 존재해야 합니다.");
    }

}