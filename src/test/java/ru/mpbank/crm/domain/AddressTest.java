package ru.mpbank.crm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование контракта Address")
public class AddressTest {

    @Test
    public void shouldBeEqual_whenAllFieldsEqual() {
        Address addr1 = new Address("ул. Ленина, 1", "Москва", "101000");
        Address addr2 = new Address("ул. Ленина, 1", "Москва", "101000");

        assertThat(addr1).isEqualTo(addr2);
    }

    @Test
    public void shouldHaveSameHashCode_whenFieldsEqual() {
        Address addr1 = new Address("ул. Ленина, 1", "Москва", "101000");
        Address addr2 = new Address("ул. Ленина, 1", "Москва", "101000");

        assertThat(addr1.hashCode()).isEqualTo(addr2.hashCode());
    }

    @Test
    public void shouldNotBeEqual_whenAtLeastOneFieldDiffers() {
        Address base = new Address("ул. Ленина, 1", "Москва", "101000");

        assertThat(base).isNotEqualTo(new Address("ул. Ленина, 2", "Москва", "101000"));
        assertThat(base).isNotEqualTo(new Address("ул. Ленина, 1", "Тула", "101000"));
        assertThat(base).isNotEqualTo(new Address("ул. Ленина, 1", "Москва", "300000"));
    }

    @Test
    public void shouldBeReflexive() {
        Address addr = new Address("ул. Ленина, 1", "Москва", "101000");

        assertThat(addr).isEqualTo(addr);
    }

    @Test
    public void shouldBeSymmetric() {
        Address addr1 = new Address("ул. Ленина, 1", "Москва", "101000");
        Address addr2 = new Address("ул. Ленина, 1", "Москва", "101000");

        assertThat(addr1.equals(addr2)).isEqualTo(addr2.equals(addr1));
    }

    @Test
    public void shouldBeConsistent_onRepeatedCalls() {
        Address addr1 = new Address("ул. Ленина, 1", "Москва", "101000");
        Address addr2 = new Address("ул. Ленина, 1", "Москва", "101000");

        assertThat(addr1.equals(addr2)).isEqualTo(addr1.equals(addr2));
        assertThat(addr1.hashCode()).isEqualTo(addr1.hashCode());
    }

    @Test
    public void shouldNotBeEqualToNullOrOtherType() {
        Address addr = new Address("ул. Ленина, 1", "Москва", "101000");

        assertThat(addr).isNotEqualTo(null);
        assertThat(addr).isNotEqualTo("ул. Ленина, 1");
    }

    @Test
    public void shouldBeFoundInMap_byEqualValueObject() {
        Map<Address, String> notes = new HashMap<>();
        notes.put(new Address("ул. Ленина, 1", "Москва", "101000"), "головной офис");

        String found = notes.get(new Address("ул. Ленина, 1", "Москва", "101000"));

        assertThat(found).isEqualTo("головной офис");
    }
}
