package ru.mpbank.crm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование контракта Deal")
public class DealTest {

    private static Deal deal(UUID id, UUID leadId, BigDecimal amount, DealStage stage) {
        return Deal.builder()
                .id(id)
                .leadId(leadId)
                .amount(amount)
                .stage(stage)
                .createdAt(Instant.now())
                .build();
    }

    @Test
    public void shouldBeEqual_whenIdEqual_evenIfAmountDiffers() {
        UUID id = UUID.randomUUID();
        UUID leadId = UUID.randomUUID();
        Deal a = deal(id, leadId, new BigDecimal("100.50"), DealStage.OPEN);
        Deal b = deal(id, leadId, new BigDecimal("999999.99"), DealStage.WON);

        assertThat(a).isEqualTo(b);
    }

    @Test
    public void shouldNotBeEqual_whenIdDiffers_evenIfAmountEqual() {
        UUID leadId = UUID.randomUUID();
        Deal a = deal(UUID.randomUUID(), leadId, new BigDecimal("100.50"), DealStage.OPEN);
        Deal b = deal(UUID.randomUUID(), leadId, new BigDecimal("100.50"), DealStage.OPEN);

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    public void shouldHaveSameHashCode_whenIdsEqual() {
        UUID id = UUID.randomUUID();
        UUID leadId = UUID.randomUUID();
        Deal a = deal(id, leadId, new BigDecimal("100.50"), DealStage.OPEN);
        Deal b = deal(id, leadId, new BigDecimal("5000000.00"), DealStage.LOST);

        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    public void shouldBeReflexive() {
        Deal a = deal(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100.50"), DealStage.OPEN);

        assertThat(a).isEqualTo(a);
    }

    @Test
    public void shouldBeSymmetric() {
        UUID id = UUID.randomUUID();
        UUID leadId = UUID.randomUUID();
        Deal a = deal(id, leadId, new BigDecimal("100.50"), DealStage.OPEN);
        Deal b = deal(id, leadId, new BigDecimal("999.99"), DealStage.WON);

        assertThat(a.equals(b)).isEqualTo(b.equals(a));
    }

    @Test
    public void shouldBeConsistent_onRepeatedCalls() {
        UUID id = UUID.randomUUID();
        UUID leadId = UUID.randomUUID();
        Deal a = deal(id, leadId, new BigDecimal("100.50"), DealStage.OPEN);
        Deal b = deal(id, leadId, new BigDecimal("999.99"), DealStage.WON);

        assertThat(a.equals(b)).isEqualTo(a.equals(b));
        assertThat(a.hashCode()).isEqualTo(a.hashCode());
    }

    @Test
    public void shouldNotBeEqualToNullOrOtherType() {
        Deal a = deal(UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("100.50"), DealStage.OPEN);

        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("100.50");
    }

    @Test
    public void shouldDeduplicateEqualEntities_inSet() {
        UUID id = UUID.randomUUID();
        UUID leadId = UUID.randomUUID();
        Set<Deal> deals = new HashSet<>();
        deals.add(deal(id, leadId, new BigDecimal("100.50"), DealStage.OPEN));
        deals.add(deal(id, leadId, new BigDecimal("5000000.00"), DealStage.LOST));

        assertThat(deals).hasSize(1);
    }

    @Test
    public void shouldBeFoundInMap_byEqualEntity() {
        UUID id = UUID.randomUUID();
        UUID leadId = UUID.randomUUID();
        Map<Deal, String> notes = new HashMap<>();
        notes.put(deal(id, leadId, new BigDecimal("100.50"), DealStage.OPEN), "комиссия");

        String found = notes.get(deal(id, leadId, new BigDecimal("999999.99"), DealStage.WON));

        assertThat(found).isEqualTo("комиссия");
    }
}
