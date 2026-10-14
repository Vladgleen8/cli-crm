package ru.mpbank.crm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование перечисления DealStage")
public class DealStageTest {

    @Test
    public void shouldContainExpectedValues() {
        assertThat(DealStage.values())
                .containsExactly(DealStage.OPEN, DealStage.WON, DealStage.LOST);
    }

    @Test
    public void shouldHaveDefaultStageOpen() {
        assertThat(DealStage.DEFAULT).isEqualTo(DealStage.OPEN);
    }
}
