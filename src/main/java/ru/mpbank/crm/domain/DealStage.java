package ru.mpbank.crm.domain;

public enum DealStage {
    OPEN,
    WON,
    LOST;

    public static final DealStage DEFAULT = OPEN;
}
