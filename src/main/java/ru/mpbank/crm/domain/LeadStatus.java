package ru.mpbank.crm.domain;

public enum LeadStatus {
    NEW,
    QUALIFIED,
    CONVERTED,
    REJECTED;

    public static final LeadStatus DEFAULT = NEW;
}
