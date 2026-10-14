package ru.mpbank.crm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование перечисления LeadSource")
public class LeadSourceTest {

    @Test
    public void shouldContainExpectedValues() {
        assertThat(LeadSource.values())
                .containsExactly(LeadSource.WEBSITE, LeadSource.CALL, LeadSource.PARTNER, LeadSource.REFERRAL);
    }
}
