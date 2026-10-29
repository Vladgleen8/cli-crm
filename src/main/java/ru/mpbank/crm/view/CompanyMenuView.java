package ru.mpbank.crm.view;

import ru.mpbank.crm.domain.Company;
import ru.mpbank.crm.service.CompanyService;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class CompanyMenuView {

    private final CompanyService service;
    private final Scanner scanner = new Scanner(System.in);

    public CompanyMenuView(CompanyService service) {
        this.service = service;
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> handleCreate();
                case "2" -> handleFind();
                case "3" -> handleList();
                case "4" -> handleDelete();
                case "0" -> running = false;
                default  -> System.out.println("Неизвестный пункт меню");
            }
        }
        System.out.println("До свидания");
    }

    private void printMenu() {
        System.out.println("""
            === CRM компании ===
            1. Создать компанию
            2. Найти компанию по ID
            3. Список компаний
            4. Удалить компанию
            0. Выход""");
        System.out.print("Выберите пункт: ");
    }

    private void handleCreate() {
        System.out.print("Наименование: ");
        String name = scanner.nextLine().trim();
        System.out.print("ИНН: ");
        String inn = scanner.nextLine().trim();

        try {
            Company company = service.create(name, inn);
            System.out.println("Компания сохранена: " + company.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

    }

    private void handleFind() {
        System.out.print("ID: ");
        String raw = scanner.nextLine().trim();
        UUID id;
        try {
            id = UUID.fromString(raw);
        } catch (IllegalArgumentException e) {
            System.out.println("Это не похоже на UUID");   // ошибка формата ввода — сервис не зовём
            return;
        }
        service.findById(id)
                .ifPresentOrElse(
                        c -> System.out.println("Найдено: " + c.getName() + " (ИНН " + c.getInn() + ")"),
                        () -> System.out.println("Компания с ID " + id + " не найдена"));
    }

    private void handleList() {
        List<Company> companies = service.findAll();        // сервис только отдал коллекцию
        if (companies.isEmpty()) {
            System.out.println("Компаний пока нет");
            return;
        }
        companies.stream()                                  // конвейер поверх списка
                .map(c -> c.getId() + " │ " + c.getName() + " │ ИНН " + c.getInn())
                .forEach(System.out::println);             // печать — ответственность view
    }

    private void handleDelete() {
        System.out.print("ID: ");
        String raw = scanner.nextLine().trim();
        UUID id;
        try {
            id = UUID.fromString(raw);
        } catch (IllegalArgumentException e) {
            System.out.println("Это не похоже на UUID");
            return;
        }
        service.delete(id);
    }
}