package ru.mpbank.crm.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тестирование контракта Company")
public class CompanyTest {

    private static Company company(UUID id, String name, String inn) {
        return Company.builder()
                .id(id)
                .name(name)
                .inn(inn)
                .ogrn("1027700132195")
                .form(OwnershipForm.OOO)
                .address(new Address("ул. Ленина, 1", "Москва", "101000"))
                .build();
    }

    @Test
    public void shouldBeEqual_whenIdEqual_evenIfFieldsDiffer() {
        UUID id = UUID.randomUUID();
        Company a = company(id, "ООО Ромашка", "7707083793");
        Company b = company(id, "ООО Одуванчик", "5001000010");

        assertThat(a).isEqualTo(b);
    }

    @Test
    public void shouldNotBeEqual_whenIdDiffers_evenIfFieldsEqual() {
        Company a = company(UUID.randomUUID(), "ООО Ромашка", "7707083793");
        Company b = company(UUID.randomUUID(), "ООО Ромашка", "7707083793");

        assertThat(a).isNotEqualTo(b);
    }

    @Test
    public void shouldHaveSameHashCode_whenIdsEqual() {
        UUID id = UUID.randomUUID();
        Company a = company(id, "ООО Ромашка", "7707083793");
        Company b = company(id, "ООО Одуванчик", "5001000010");

        assertThat(a.hashCode()).isEqualTo(b.hashCode());
    }

    @Test
    public void shouldBeReflexive() {
        Company a = company(UUID.randomUUID(), "ООО Ромашка", "7707083793");

        assertThat(a).isEqualTo(a);
    }

    @Test
    public void shouldBeSymmetric() {
        UUID id = UUID.randomUUID();
        Company a = company(id, "ООО Ромашка", "7707083793");
        Company b = company(id, "ООО Одуванчик", "5001000010");

        assertThat(a.equals(b)).isEqualTo(b.equals(a));
    }

    @Test
    public void shouldBeConsistent_onRepeatedCalls() {
        UUID id = UUID.randomUUID();
        Company a = company(id, "ООО Ромашка", "7707083793");
        Company b = company(id, "ООО Одуванчик", "5001000010");

        assertThat(a.equals(b)).isEqualTo(a.equals(b));
        assertThat(a.hashCode()).isEqualTo(a.hashCode());
    }

    @Test
    public void shouldNotBeEqualToNullOrOtherType() {
        Company a = company(UUID.randomUUID(), "ООО Ромашка", "7707083793");

        assertThat(a).isNotEqualTo(null);
        assertThat(a).isNotEqualTo("ООО Ромашка");
    }

    @Test
    public void shouldDeduplicateEqualEntities_inSet() {
        UUID id = UUID.randomUUID();
        Set<Company> companies = new HashSet<>();
        companies.add(company(id, "ООО Ромашка", "7707083793"));
        companies.add(company(id, "ООО Одуванчик", "5001000010"));

        assertThat(companies).hasSize(1);
    }

    @Test
    public void shouldBeFoundInMap_byEqualEntity() {
        UUID id = UUID.randomUUID();
        Map<Company, String> notes = new HashMap<>();
        notes.put(company(id, "ООО Ромашка", "7707083793"), "VIP-клиент");

        String found = notes.get(company(id, "ООО Ромашка-старое", "7707083793"));

        assertThat(found).isEqualTo("VIP-клиент");
    }
}
