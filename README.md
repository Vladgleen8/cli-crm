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