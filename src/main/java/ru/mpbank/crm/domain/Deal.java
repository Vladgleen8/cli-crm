package ru.mpbank.crm.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@ToString
public class Deal {
    private final UUID id;
    private final UUID leadId;
    private BigDecimal amount;
    private DealStage stage;
    private Instant createdAt;
}
