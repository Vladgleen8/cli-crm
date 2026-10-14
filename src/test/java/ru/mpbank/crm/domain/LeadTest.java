package ru.mpbank.crm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование контракта Lead")
public class LeadTest {

    private static Lead lead(UUID id, String title, LeadSource source, LeadStatus status) {
        return Lead.builder()
                .id(id)
                .title(title)
                .source(source)
                .status(status)
                .contact(Contact.builder().id(UUID.randomUUID()).build())
                .createdAt(Instant.now())
                .build();
    }

    @Test
    public void shouldBeEqual_whenIdEqual_evenIfFieldsDiffer() {
        UUID id = UUID.randomUUID();
        Lead a = lead(id, "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW);
        Lead b = lead(id, "Совсем другое название", LeadSource.CALL, LeadStatus.QUALIFIED);

        assertThat(a).isEqualTo(b);
    }

    @Test
    public void shouldNotBeEqual_whenIdDiffers_evenIfFieldsEqual() {
        Lead a = lead(UUID.randomUUID(), "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW);
        Lead b = lead(UUID.randomUUID(), "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW);

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    public void shouldHaveSameHashCode_whenIdsEqual() {
        UUID id = UUID.randomUUID();
        Lead a = lead(id, "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW);
        Lead b = lead(id, "Другое название", LeadSource.PARTNER, LeadStatus.REJECTED);

        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    public void shouldBeReflexive() {
        Lead a = lead(UUID.randomUUID(), "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW);

        assertThat(a).isEqualTo(a);
    }

    @Test
    public void shouldBeSymmetric() {
        UUID id = UUID.randomUUID();
        Lead a = lead(id, "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW);
        Lead b = lead(id, "Другое название", LeadSource.CALL, LeadStatus.QUALIFIED);

        assertThat(a.equals(b)).isEqualTo(b.equals(a));
    }

    @Test
    public void shouldBeConsistent_onRepeatedCalls() {
        UUID id = UUID.randomUUID();
        Lead a = lead(id, "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW);
        Lead b = lead(id, "Другое название", LeadSource.CALL, LeadStatus.QUALIFIED);

        assertThat(a.equals(b)).isEqualTo(a.equals(b));
        assertThat(a.hashCode()).isEqualTo(a.hashCode());
    }

    @Test
    public void shouldNotBeEqualToNullOrOtherType() {
        Lead a = lead(UUID.randomUUID(), "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW);

        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("Заявка на кредит");
    }

    @Test
    public void shouldDeduplicateEqualEntities_inSet() {
        UUID id = UUID.randomUUID();
        Set<Lead> leads = new HashSet<>();
        leads.add(lead(id, "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW));
        leads.add(lead(id, "Другая заявка", LeadSource.CALL, LeadStatus.QUALIFIED));

        assertThat(leads).hasSize(1);
    }

    @Test
    public void shouldBeFoundInMap_byEqualEntity() {
        UUID id = UUID.randomUUID();
        Map<Lead, String> notes = new HashMap<>();
        notes.put(lead(id, "Заявка на кредит", LeadSource.WEBSITE, LeadStatus.NEW), "горячий лид");

        String found = notes.get(lead(id, "Совсем другое название", LeadSource.CALL, LeadStatus.QUALIFIED));

        assertThat(found).isEqualTo("горячий лид");
    }
}
