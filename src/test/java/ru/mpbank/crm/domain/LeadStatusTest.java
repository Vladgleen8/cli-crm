package ru.mpbank.crm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование перечисления LeadStatus")
public class LeadStatusTest {

    @Test
    public void shouldContainExpectedValues() {
        assertThat(LeadStatus.values())
                .containsExactly(LeadStatus.NEW, LeadStatus.QUALIFIED, LeadStatus.CONVERTED, LeadStatus.REJECTED);
    }

    @Test
    public void shouldHaveDefaultStatusNew() {
        assertThat(LeadStatus.DEFAULT).isEqualTo(LeadStatus.NEW);
    }
}
