# CLI-CRM (CCRM)

Консольная CRM «лиды» для МП Банка — учебный проект bcore-CLI.
Чистая Java, трёхслойная архитектура, хранилище в памяти.

## Требования
- JDK 21 (Eclipse Temurin)

## Сборка и запуск
- `./gradlew build` — собрать проект
- `./gradlew run` — запустить приложение
- `./gradlew check` — тесты + проверка стиля (Checkstyle)


## Структура проекта
- domain	доменная модель	сущности Lead/Contact/Address, value-объекты, enums (CCRM-2)
- repository	хранение за интерфейсом	LeadRepository (интерфейс) + InMemoryLeadRepository (CCRM-3)
- service	бизнес-логика	дедуп, валидация, переходы воронки (Спринт 2)
- view	консольное меню	сбор ввода, вызов сервиса, вывод (CCRM-5)
- app	composition root	Main — сборка графа объектов руками