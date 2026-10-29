package ru.mpbank.crm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mpbank.crm.domain.Company;
import ru.mpbank.crm.repository.InMemoryCompanyRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование CompanyService")
public class CompanyServiceTest {

    @Test
    @DisplayName("создание новой компании возвращает её с id и кладёт в репозиторий")
    void create_thenReturnsCompanyWithId_andRepoHasIt() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);

        // when
        Company result = service.create("ООО Ромашка", "7707083793");

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("create: при создании заполняются name и inn")
    void create_thenHasNameAndInn() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);

        // when
        Company result = service.create("ООО Ромашка", "7707083793");

        // then
        assertThat(result.getName()).isEqualTo("ООО Ромашка");
        assertThat(result.getInn()).isNotBlank();
    }

    @Test
    @DisplayName("create: некорректный ИНН бросает ошибку")
    void create_whenInnInvalid_thenThrowException() {
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);

        assertThatThrownBy(() -> service.create("ООО Ромашка", "770708379")).isInstanceOf(IllegalArgumentException.class);
        assertThat(repo.findAll()).isEmpty();
    }

    @Test
    @DisplayName("create: при повторном вызове с тем же ИНН возвращает существующую компанию, дубль не создаётся")
    void create_whenInnExists_thenReturnsExistingAndDoesNotCreateDuplicate() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);
        Company first = service.create("ООО Ромашка", "7707083793");

        // when
        Company second = service.create("ООО Ромашка-Другое", "7707083793");

        // then
        assertThat(second).isSameAs(first);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("create: нормализация ИНН (пробелы/символы) не создаёт дубль")
    void create_whenInnFormattedDifferently_thenReturnsExisting() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);
        Company first = service.create("ООО Ромашка", "7707083793");

        // when
        Company second = service.create("ООО Ромашка", "7707 0837 93");

        // then
        assertThat(second).isSameAs(first);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("create: разные ИНН создают разные компании")
    void create_whenDifferentInn_thenCreatesBoth() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);

        // when
        Company first = service.create("ООО Ромашка", "7707083793");
        Company second = service.create("ООО Одуванчик", "5001000010");

        // then
        assertThat(first).isNotSameAs(second);
        assertThat(repo.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("findById: возвращает компанию, если найдена")
    void findById_whenExists_thenReturnsCompany() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);
        Company created = service.create("ООО Ромашка", "7707083793");

        // when
        var found = service.findById(created.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get()).isSameAs(created);
    }

    @Test
    @DisplayName("findById: отсутствующий id → пустой Optional")
    void findById_whenNotFound_thenEmpty() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);

        // when
        var found = service.findById(UUID.randomUUID());

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findAll: пустой репозиторий → пустой список")
    void findAll_whenEmpty_thenEmpty() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);

        // when
        var all = service.findAll();

        // then
        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("findAll: возвращает все компании из репозитория")
    void findAll_thenReturnsAllFromRepository() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);
        Company first = service.create("ООО Ромашка", "7707083793");
        Company second = service.create("ООО Одуванчик", "5001000010");

        // when
        var all = service.findAll();

        // then
        assertThat(all).hasSize(2).containsExactlyInAnyOrder(first, second);
    }

    @Test
    @DisplayName("delete: удаляет существующую компанию и возвращает true")
    void delete_whenExists_thenRemovesAndReturnsTrue() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);
        Company created = service.create("ООО Ромашка", "7707083793");

        // when
        boolean removed = service.delete(created.getId());

        // then
        assertThat(removed).isTrue();
        assertThat(repo.findById(created.getId())).isEmpty();
        assertThat(repo.findAll()).isEmpty();
    }

    @Test
    @DisplayName("delete: отсутствующий id → false, хранилище не меняется")
    void delete_whenNotFound_thenFalse() {
        // given
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        CompanyService service = new CompanyService(repo);
        service.create("ООО Ромашка", "7707083793");

        // when
        boolean removed = service.delete(UUID.randomUUID());

        // then
        assertThat(removed).isFalse();
        assertThat(repo.findAll()).hasSize(1);
    }
}
