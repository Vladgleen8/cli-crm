package ru.mpbank.crm.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mpbank.crm.domain.Company;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование InMemoryCompanyRepository")
class InMemoryCompanyRepositoryTest {

    private static Company company(UUID id, String name, String inn) {
        return Company.builder()
                .id(id)
                .name(name)
                .inn(inn)
                .build();
    }

    @Test
    @DisplayName("save: сохранённая компания находится по своему id")
    void save_thenFindByIdReturnsSavedCompany() {
        // given
        var repo = new InMemoryCompanyRepository();
        var company = company(UUID.randomUUID(), "ООО Ромашка", "7707083793");

        // when
        repo.save(company);

        // then
        assertThat(repo.findById(company.getId())).contains(company);
    }

    @Test
    @DisplayName("save: возвращает сохранённую компанию")
    void save_thenReturnsSavedCompany() {
        // given
        var repo = new InMemoryCompanyRepository();
        var company = company(UUID.randomUUID(), "ООО Ромашка", "7707083793");

        // when
        var saved = repo.save(company);

        // then
        assertThat(saved).isEqualTo(company);
    }

    @Test
    @DisplayName("findById: отсутствующий id → пустой Optional")
    void findById_whenNotFound_thenEmpty() {
        // given
        var repo = new InMemoryCompanyRepository();

        // when
        var found = repo.findById(UUID.randomUUID());

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findAll: пустой репозиторий → пустой список")
    void findAll_whenEmpty_thenEmpty() {
        // given
        var repo = new InMemoryCompanyRepository();

        // when
        List<Company> all = repo.findAll();

        // then
        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("findAll: возвращает все сохранённые компании")
    void findAll_thenReturnsAllSaved() {
        // given
        var repo = new InMemoryCompanyRepository();
        var first = company(UUID.randomUUID(), "ООО Ромашка", "7707083793");
        var second = company(UUID.randomUUID(), "ООО Одуванчик", "5001000010");

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
        var repo = new InMemoryCompanyRepository();
        var id = UUID.randomUUID();

        // when
        repo.save(company(id, "ООО Ромашка", "7707083793"));
        repo.save(company(id, "ООО Одуванчик", "5001000010"));

        // then
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("findAll: отдаёт копию, изменение списка не влияет на хранилище")
    void findAll_thenReturnsCopy() {
        // given
        var repo = new InMemoryCompanyRepository();
        repo.save(company(UUID.randomUUID(), "ООО Ромашка", "7707083793"));

        // when
        var all = repo.findAll();
        all.clear();

        // then
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("deleteById: удаляет существующую компанию и возвращает true")
    void deleteById_whenExists_thenRemovesAndReturnsTrue() {
        // given
        var repo = new InMemoryCompanyRepository();
        var company = company(UUID.randomUUID(), "ООО Ромашка", "7707083793");
        repo.save(company);

        // when
        var removed = repo.deleteById(company.getId());

        // then
        assertThat(removed).isTrue();
        assertThat(repo.findById(company.getId())).isEmpty();
        assertThat(repo.findAll()).isEmpty();
    }

    @Test
    @DisplayName("deleteById: отсутствующий id → false, хранилище не меняется")
    void deleteById_whenNotFound_thenFalse() {
        // given
        var repo = new InMemoryCompanyRepository();
        repo.save(company(UUID.randomUUID(), "ООО Ромашка", "7707083793"));

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
        var repo = new InMemoryCompanyRepository();
        var id = UUID.randomUUID();
        repo.save(company(id, "ООО Ромашка", "7707083793"));
        var updated = company(id, "ООО Ромашка-Новая", "7707083793");

        // when
        repo.update(updated);

        // then
        assertThat(repo.findById(id)).contains(updated);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("update: возвращает обновлённую компанию")
    void update_thenReturnsUpdatedCompany() {
        // given
        var repo = new InMemoryCompanyRepository();
        var id = UUID.randomUUID();
        repo.save(company(id, "ООО Ромашка", "7707083793"));
        var updated = company(id, "ООО Ромашка-Новая", "7707083793");

        // when
        var result = repo.update(updated);

        // then
        assertThat(result).isEqualTo(updated);
    }

    @Test
    @DisplayName("update: выбрасывает исключение если нет компании")
    void update_whenNotFound_thenThrowsException() {
        var repo = new InMemoryCompanyRepository();
        var id = UUID.randomUUID();
        var updated = company(id, "ООО Ромашка-Новая", "7707083793");

        assertThatThrownBy(() -> repo.update(updated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Company not found: " + id);
    }
}
