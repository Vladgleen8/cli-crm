package ru.mpbank.crm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование контракта Contact")
public class ContactTest {

    private static Contact contact(UUID id, String email, String phone) {
        return Contact.builder()
                .id(id)
                .firstName("Иван")
                .lastName("Петров")
                .email(email)
                .phone(phone)
                .address(new Address("ул. Ленина, 1", "Москва", "101000"))
                .build();
    }

    @Test
    public void shouldBeEqual_whenIdEqual_evenIfFieldsDiffer() {
        UUID id = UUID.randomUUID();
        Contact a = contact(id, "ivan@example.com", "8 (912) 345-67-89");
        Contact b = contact(id, "other@example.com", "+79990000000");

        assertThat(a).isEqualTo(b);
    }

    @Test
    public void shouldNotBeEqual_whenIdDiffers_evenIfDedupKeysEqual() {
        Contact a = contact(UUID.randomUUID(), "ivan@example.com", "8 (912) 345-67-89");
        Contact b = contact(UUID.randomUUID(), "ivan@example.com", "8 (912) 345-67-89");

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    public void shouldHaveSameHashCode_whenIdsEqual() {
        UUID id = UUID.randomUUID();
        Contact a = contact(id, "ivan@example.com", "8 (912) 345-67-89");
        Contact b = contact(id, "other@example.com", "+79990000000");

        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    public void shouldBeReflexive() {
        Contact a = contact(UUID.randomUUID(), "ivan@example.com", "8 (912) 345-67-89");

        assertThat(a).isEqualTo(a);
    }

    @Test
    public void shouldBeSymmetric() {
        UUID id = UUID.randomUUID();
        Contact a = contact(id, "ivan@example.com", "8 (912) 345-67-89");
        Contact b = contact(id, "other@example.com", "+79990000000");

        assertThat(a.equals(b)).isEqualTo(b.equals(a));
    }

    @Test
    public void shouldBeConsistent_onRepeatedCalls() {
        UUID id = UUID.randomUUID();
        Contact a = contact(id, "ivan@example.com", "8 (912) 345-67-89");
        Contact b = contact(id, "other@example.com", "+79990000000");

        assertThat(a.equals(b)).isEqualTo(a.equals(b));
        assertThat(a.hashCode()).isEqualTo(a.hashCode());
    }

    @Test
    public void shouldNotBeEqualToNullOrOtherType() {
        Contact a = contact(UUID.randomUUID(), "ivan@example.com", "8 (912) 345-67-89");

        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("ivan@example.com");
    }

    @Test
    public void shouldDeduplicateEqualEntities_inSet() {
        UUID id = UUID.randomUUID();
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact(id, "ivan@example.com", "8 (912) 345-67-89"));
        contacts.add(contact(id, "other@example.com", "+79990000000"));

        assertThat(contacts).hasSize(1);
    }

    @Test
    public void shouldBeFoundInMap_byEqualEntity() {
        UUID id = UUID.randomUUID();
        Map<Contact, String> notes = new HashMap<>();
        notes.put(contact(id, "ivan@example.com", "8 (912) 345-67-89"), "ЛПР");

        String found = notes.get(contact(id, "other@example.com", "+79990000000"));

        assertThat(found).isEqualTo("ЛПР");
    }
}
