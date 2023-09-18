package com.lemontree.wallet.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Table(name = "user")
@Entity
public class User {
    @Column(name = "id", columnDefinition = "binary(16)")
    @Id
    private UUID id;

    private Boolean active;

    public UUID getId() {
        return id;
    }


    protected User() {

    }

    public User(UUID id, Boolean active) {
        if (Objects.isNull(active)) {
            throw new IllegalArgumentException("유저의 상태는 필수로 존재해야 합니다.");
        }
        this.id = id;
        this.active = active;
    }

}
