package com.project.backend.transactions.models;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum TransactionType {
    IN("IN", "INBOUND", 1),
    OUT("OUT", "OUTBOUND", -1),
    MOVE("MOVE", "MOVE", 0);

    private final String shortName;
    private final String fullName;
    private final int code;

    TransactionType(String shortName, String fullName, int code) {
        this.shortName = shortName;
        this.fullName = fullName;
        this.code = code;
    }

    public static Optional<TransactionType> getAccountStatusByValue(String value) {
        return Arrays.stream(TransactionType.values())
                .filter(accStatus -> accStatus.shortName.equals(value)
                        || accStatus.fullName.equals(value))
                .findFirst();
    }

    public static Optional<TransactionType> getAccountStatusByValue(int value) {
        return Arrays.stream(TransactionType.values())
                .filter(accStatus -> accStatus.code == value)
                .findFirst();
    }
}
