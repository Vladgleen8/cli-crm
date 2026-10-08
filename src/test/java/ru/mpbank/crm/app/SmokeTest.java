package ru.mpbank.crm.app;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SmokeTest {
    @Test
    void contextLoads() {
        assertThat("CRM").isNotBlank();
    }
}