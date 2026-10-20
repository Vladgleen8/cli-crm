package ru.mpbank.crm.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mpbank.crm.domain.Contact;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Тестирование InMemoryContactRepository")
class InMemoryContactRepositoryTest {

    private static Contact contact(String firstName, String lastName, String email, String phone) {
        return Contact.builder()
                .id(UUID.randomUUID())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .build();
    }

    @Test
    @DisplayName("save: сохранённый контакт находится по своему id")
    void save_thenFindByIdReturnsSavedContact() {
        // given
        var repo = new InMemoryContactRepository();
        var contact = contact("Иван", "Петров", "ivan@mail.ru", "+79123456789");

        // when
        repo.save(contact);

        // then
        assertThat(repo.findById(contact.getId())).contains(contact);
    }

    @Test
    @DisplayName("findById: отсутствующий id → пустой Optional")
    void findById_whenNotFound_thenEmpty() {
        // given
        var repo = new InMemoryContactRepository();

        // when
        var found = repo.findById(UUID.randomUUID());

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findAll: пустой репозиторий → пустой список")
    void findAll_whenEmpty_thenEmpty() {
        // given
        var repo = new InMemoryContactRepository();

        // when
        List<Contact> all = repo.findAll();

        // then
        assertThat(all).isEmpty();
    }

    @Test
    @DisplayName("findAll: возвращает все сохранённые контакты")
    void findAll_thenReturnsAllSaved() {
        // given
        var repo = new InMemoryContactRepository();
        var first = contact("Иван", "Петров", "ivan@mail.ru", "+79123456789");
        var second = contact("Пётр", "Сидоров", "petr@mail.ru", "+79001112233");

        // when
        repo.save(first);
        repo.save(second);

        // then
        assertThat(repo.findAll()).hasSize(2).contains(first, second);
    }

    @Test
    @DisplayName("findAll: отдаёт копию, изменение списка не влияет на хранилище")
    void findAll_thenReturnsCopy() {
        // given
        var repo = new InMemoryContactRepository();
        repo.save(contact("Иван", "Петров", "ivan@mail.ru", "+79123456789"));

        // when
        var all = repo.findAll();
        all.clear();

        // then
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("deleteById: удаляет существующий контакт и возвращает true")
    void deleteById_whenExists_thenRemovesAndReturnsTrue() {
        // given
        var repo = new InMemoryContactRepository();
        var contact = contact("Иван", "Петров", "ivan@mail.ru", "+79123456789");
        repo.save(contact);

        // when
        var removed = repo.deleteById(contact.getId());

        // then
        assertThat(removed).isTrue();
        assertThat(repo.findById(contact.getId())).isEmpty();
        assertThat(repo.findAll()).isEmpty();
    }

    @Test
    @DisplayName("deleteById: отсутствующий id → false, хранилище не меняется")
    void deleteById_whenNotFound_thenFalse() {
        // given
        var repo = new InMemoryContactRepository();
        repo.save(contact("Иван", "Петров", "ivan@mail.ru", "+79123456789"));

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
        var repo = new InMemoryContactRepository();
        var id = UUID.randomUUID();
        repo.save(Contact.builder().id(id).firstName("Иван").lastName("Петров").build());
        var updated = Contact.builder().id(id).firstName("Иван").lastName("Сидоров").build();

        // when
        repo.update(updated);

        // then
        assertThat(repo.findById(id)).contains(updated);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("saveOrGet: email в разном регистре и с пробелами даёт один контакт")
    void saveOrGet_whenEmailDiffersByCaseAndSpaces_thenReturnsSameContact() {
        // given
        var repo = new InMemoryContactRepository();
        var first = contact("Иван", "Петров", " Ivan@Mail.RU ", null);
        var duplicate = contact("Иван", "Петров", "ivan@mail.ru", null);

        // when
        var stored = repo.saveOrGet(first);
        var result = repo.saveOrGet(duplicate);

        // then
        assertThat(result).isEqualTo(stored);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("saveOrGet: телефон в разных записях даёт один контакт")
    void saveOrGet_whenPhoneDiffersByFormat_thenReturnsSameContact() {
        // given
        var repo = new InMemoryContactRepository();
        var first = contact("Иван", "Петров", null, "8 (912) 345-67-89");
        var duplicate = contact("Иван", "Петров", null, "+7 912 345 67 89");
        var anotherDuplicate = contact("Иван", "Петров", null, "9123456789");

        // when
        var stored = repo.saveOrGet(first);
        var second = repo.saveOrGet(duplicate);
        var third = repo.saveOrGet(anotherDuplicate);

        // then
        assertThat(second).isEqualTo(stored);
        assertThat(third).isEqualTo(stored);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("saveOrGet: совпадение по любому ключу (email) даёт дубль")
    void saveOrGet_whenEmailMatches_thenDuplicateRegardlessOfPhone() {
        // given
        var repo = new InMemoryContactRepository();
        var first = contact("Иван", "Петров", "ivan@mail.ru", "+79123456789");
        var duplicate = contact("Иван", "Петров", "ivan@mail.ru", "+79001112233");

        // when
        var stored = repo.saveOrGet(first);
        var result = repo.saveOrGet(duplicate);

        // then
        assertThat(result).isEqualTo(stored);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("saveOrGet: совпадение по любому ключу (телефон) даёт дубль")
    void saveOrGet_whenPhoneMatches_thenDuplicateRegardlessOfEmail() {
        // given
        var repo = new InMemoryContactRepository();
        var first = contact("Иван", "Петров", "ivan@mail.ru", "+79123456789");
        var duplicate = contact("Иван", "Петров", "other@mail.ru", "+79123456789");

        // when
        var stored = repo.saveOrGet(first);
        var result = repo.saveOrGet(duplicate);

        // then
        assertThat(result).isEqualTo(stored);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("saveOrGet: разные ключи → два разных контакта")
    void saveOrGet_whenBothKeysDiffer_thenCreatesSeparateContacts() {
        // given
        var repo = new InMemoryContactRepository();
        var first = contact("Иван", "Петров", "ivan@mail.ru", "+79123456789");
        var second = contact("Пётр", "Сидоров", "petr@mail.ru", "+79001112233");

        // when
        var storedFirst = repo.saveOrGet(first);
        var storedSecond = repo.saveOrGet(second);

        // then
        assertThat(storedSecond).isNotEqualTo(storedFirst);
        assertThat(repo.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("saveOrGet: неполные контакты (без обоих ключей) не склеиваются")
    void saveOrGet_whenBothKeysMissing_thenCreatesSeparateContacts() {
        // given
        var repo = new InMemoryContactRepository();
        var first = contact("Иван", "Петров", null, null);
        var second = contact("Пётр", "Сидоров", null, null);

        // when
        var storedFirst = repo.saveOrGet(first);
        var storedSecond = repo.saveOrGet(second);

        // then
        assertThat(storedSecond).isNotEqualTo(storedFirst);
        assertThat(repo.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("saveOrGet: неполный контакт хранится и находится по id")
    void saveOrGet_whenBothKeysMissing_thenStoredInStorage() {
        // given
        var repo = new InMemoryContactRepository();
        var contact = contact("Иван", "Петров", null, null);

        // when
        var stored = repo.saveOrGet(contact);

        // then
        assertThat(repo.findById(contact.getId())).contains(stored);
        assertThat(repo.findAll()).hasSize(1);
    }

    @Test
    @DisplayName("saveOrGet: битый телефон → IllegalArgumentException")
    void saveOrGet_whenPhoneInvalid_thenThrows() {
        // given
        var repo = new InMemoryContactRepository();
        var broken = contact("Иван", "Петров", null, "12345");

        // when / then
        assertThatThrownBy(() -> repo.saveOrGet(broken))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
