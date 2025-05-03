package com.blackstone.account.entity.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum AccountType {
    SALARY("salary"), SAVING("saving"), INVESTMENT("investment");

    private final String name;

    AccountType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator
    public static AccountType forValue(String value) {
        return Arrays.stream(AccountType.values())
                .filter(type -> type.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid account type: " + value));
    }
}
