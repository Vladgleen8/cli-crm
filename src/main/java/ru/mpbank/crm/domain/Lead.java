package ru.mpbank.crm.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@ToString

public class Lead {
    private final UUID id;
    private String title;
    private LeadSource source;
    private LeadStatus status;
    private Contact contact;
    private Instant createdAt;
}
