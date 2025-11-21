package com.airxelerate.inventory.persistence.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {
    ROLE_ADMIN,
    ROLE_USER;

}
