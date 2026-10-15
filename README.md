# CLI-CRM (CCRM)

Консольная CRM «лиды» для МП Банка — учебный проект bcore-CLI.
Чистая Java, трёхслойная архитектура, хранилище в памяти.

## Требования
- JDK 21 (Eclipse Temurin)

## Сборка и запуск
- `./gradlew build` — собрать проект
- `./gradlew run` — запустить приложение
- `./gradlew test` — прогнать тесты
- `./gradlew check` — тесты + проверка стиля (Checkstyle)
- `./gradlew checkstyleMain` / `./gradlew checkstyleTest` — Checkstyle только для `main` / `test`

Отчёты: `build/reports/tests/test/index.html` (тесты), `build/reports/checkstyle/main.html` и `test.html` (стиль). Правила Checkstyle — `config/checkstyle/checkstyle.xml`, нарушение валит сборку.


## Структура проекта
- domain	доменная модель	сущности Lead/Contact/Address, value-объекты, enums (CCRM-2)
- repository	хранение за интерфейсом	LeadRepository (интерфейс) + InMemoryLeadRepository (CCRM-3)
- service	бизнес-логика	дедуп, валидация, переходы воронки (Спринт 2)
- view	консольное меню	сбор ввода, вызов сервиса, вывод (CCRM-5)
- app	composition root	Main — сборка графа объектов руками

## Какие поля входят в equals/hashCode и почему именно эти
`Contact` — равенство по `id`: два контакта с одинаковым email/телефоном, но разными `id` —
разные записи до дедупа (дедуп по email/телефону — бизнес-ключ, не идентичность).
- `Address` — равенство по всем полям: у адреса своего `id` нет, это value object.
- `Deal` — равенство по `id`: две сделки на одинаковую сумму — разные сделки, `amount` в equals
  не участвует.
- `Lead` — равенство по `id`: два лида с одинаковым `title` — разные заявки, дубль ловится по
  контакту, не по лиду.