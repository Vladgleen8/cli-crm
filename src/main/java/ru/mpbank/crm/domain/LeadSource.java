package ru.mpbank.crm.domain;

import java.util.Arrays;

public enum LeadSource {
    WEBSITE,
    CALL,
    PARTNER,
    REFERRAL;

    public static LeadSource fromString(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("Источник лида не может быть пустым");
        }
        try {
            return LeadSource.valueOf(input.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Неизвестный источник лида: '" + input + "'. Допустимые: "
                            + Arrays.toString(values()), e);
        }
    }
}
