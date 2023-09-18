package com.lemontree.wallet.fixture;

import com.lemontree.wallet.user.domain.User;

import java.util.UUID;

public class UserFixtures {

    public static User createUser() {
        return new User(UUID.randomUUID(), true);
    }
}
