package ru.mpbank.crm.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mpbank.crm.domain.Lead;
import ru.mpbank.crm.domain.LeadSource;
import ru.mpbank.crm.domain.LeadStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование InMemoryLeadRepository")
class InMemoryLeadRepositoryTest {

    private static Lead lead(UUID id, String title) {
        return Lead.builder()
                .id(id)
                .title(title)
                .source(LeadSource.WEBSITE)
                .status(LeadStatus.NEW)
                .createdAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("save: сохранённый лид находится по своему id")
    void save_thenFindByIdReturnsSavedLead() {
        // given
        var repo = new InMemoryLeadRepository();
        var lead = lead(UUID.randomUUID(), "Заявка на кредит");

        // when
        repo.save(lead);

        // then
        assertThat(repo.findById(lead.getId())).contains(lead);
    }

    @Test
    @DisplayName("save: возвращает сохранённый лид")
    void save_thenReturnsSavedLead() {
        // given
        var repo = new InMemoryLeadRepository();
        var lead = lead(UUID.randomUUID(), "Заявка на кредит");

        // when
        var saved = repo.save(lead);

        // then
        assertThat(saved).isEqualTo(lead);
    }

    @Test
    @DisplayName("findById: отсутствующий id → пустой Optional")
    void findById_whenNotFound_thenEmpty() {
        // given
        var repo = new InMemoryLeadRepository();

        // when
        var found = repo.findById(UUID.randomUUID());

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findAll: пустой репозиторий → пустой список")
    void findAll_whenEmpty_thenEmpty() {
        // given
        var repo = new InMemoryLeadRepository();

        // when
        List<Lead> all = repo.findAll();

        // then
        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("findAll: возвращает все сохранённые лиды")
    void findAll_thenReturnsAllSaved() {
        // given
        var repo = new InMemoryLeadRepository();
        var first = lead(UUID.randomUUID(), "Заявка на кредит");
        var second = lead(UUID.randomUUID(), "Заявка на карту");

        // when
        repo.save(first);
        repo.save(second);

        // then
        assertThat(repo.findAll()).hasSize(2).contains(first, second);
    }

    @Test
    @DisplayName("save: повторный save с тем же id не растит хранилище")
    void save_withSameId_thenDoesNotGrow() {
        // given
        var repo = new InMemoryLeadRepository();
        var id = UUID.randomUUID();

        // when
        repo.save(lead(id, "Заявка на кредит"));
        repo.save(lead(id, "Заявка на карту"));

        // then
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("findAll: отдаёт копию, изменение списка не влияет на хранилище")
    void findAll_thenReturnsCopy() {
        // given
        var repo = new InMemoryLeadRepository();
        repo.save(lead(UUID.randomUUID(), "Заявка на кредит"));

        // when
        var all = repo.findAll();
        all.clear();

        // then
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("deleteById: удаляет существующий лид и возвращает true")
    void deleteById_whenExists_thenRemovesAndReturnsTrue() {
        // given
        var repo = new InMemoryLeadRepository();
        var lead = lead(UUID.randomUUID(), "Заявка на кредит");
        repo.save(lead);

        // when
        var removed = repo.deleteById(lead.getId());

        // then
        assertThat(removed).isTrue();
        assertThat(repo.findById(lead.getId())).isEmpty();
        assertThat(repo.findAll()).isEmpty();
    }

    @Test
    @DisplayName("deleteById: отсутствующий id → false, хранилище не меняется")
    void deleteById_whenNotFound_thenFalse() {
        // given
        var repo = new InMemoryLeadRepository();
        repo.save(lead(UUID.randomUUID(), "Заявка на кредит"));

        // when
        var removed = repo.deleteById(UUID.randomUUID());

        // then
        assertThat(removed).isFalse();
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("update: заменяет состояние по тому же id")
    void update_thenReplacesStateById() {
        // given
        var repo = new InMemoryLeadRepository();
        var id = UUID.randomUUID();
        repo.save(lead(id, "Заявка на кредит"));
        var updated = lead(id, "Заявка на кредит (обновлена)");

        // when
        repo.update(updated);

        // then
        assertThat(repo.findById(id)).contains(updated);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("update: возвращает обновлённый лид")
    void update_thenReturnsUpdatedLead() {
        // given
        var repo = new InMemoryLeadRepository();
        var id = UUID.randomUUID();
        repo.save(lead(id, "Заявка на кредит"));
        var updated = lead(id, "Заявка на кредит (обновлена)");

        // when
        var result = repo.update(updated);

        // then
        assertThat(result).isEqualTo(updated);
    }

    @Test
    @DisplayName("update: выбрасывает исключение если нет лида")
    void update_whenNotFound_thenThrowsException() {
        // given
        var repo = new InMemoryLeadRepository();
        var id = UUID.randomUUID();
        var updated = lead(id, "Заявка на кредит (обновлена)");

        assertThatThrownBy(() -> repo.update(updated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Lead not found: " + id);
    }
}
