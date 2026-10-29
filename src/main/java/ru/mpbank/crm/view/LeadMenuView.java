package ru.mpbank.crm.view;

import ru.mpbank.crm.domain.Contact;
import ru.mpbank.crm.domain.Lead;
import ru.mpbank.crm.domain.LeadSource;
import ru.mpbank.crm.service.LeadService;
import ru.mpbank.crm.service.dto.CreateLeadResult;
import ru.mpbank.crm.service.exception.DuplicateContactException;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class LeadMenuView {
    private final LeadService leadService;
    private final Scanner scanner = new Scanner(System.in);

    public LeadMenuView(LeadService leadService) {
        this.leadService = leadService;
    }

    public void run() {
        while (true) {
            if (!scanner.hasNextLine()) {
                System.out.println("До свидания");
                break;
            }
            printMenu();
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("Введите номер пункта");
                continue;
            }
            switch (line) {
                case "1" -> createLead();
                case "2" -> findLeadById();
                case "3" -> listLeads();
                case "4" -> deleteLead();
                case "0" -> {
                    System.out.println("До свидания");
                    return;
                }
                default -> {
                    if (isNumeric(line)) {
                        System.out.println("Неизвестный пункт меню");
                    } else {
                        System.out.println("Введите номер пункта");
                    }
                }
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("=== CRM лиды ===");
        System.out.println("1. Создать лид");
        System.out.println("2. Найти лид по ID");
        System.out.println("3. Список лидов");
        System.out.println("4. Удалить лид");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт: ");
    }

    private void createLead() {
        String title = readNonEmpty("Название лида: ");
        String source = readNonEmpty("Источник лида: ");
        String firstName = readNonEmpty("Имя контакта: ");
        String lastName = readNonEmpty("Фамилия контакта: ");
        String email = readNullableLine("Email контакта (можно пусто): ");
        String phone = readNullableLine("Телефон контакта (можно пусто): ");

        try {
            Lead lead = leadService.create(title, source, firstName, lastName, email, phone);
            System.out.println("Лид создан: " + lead.getId() + ", статус NEW");
        } catch (DuplicateContactException ex) {
            Contact c = ex.getExistingContact();
            String emailStr = c.getEmail() == null ? "" : c.getEmail();
            System.out.printf("Найден существующий контакт %s %s, %s — связать лид с ним? [y/n]%n",
                    c.getFirstName(), c.getLastName(), emailStr);
            String ans = scanner.nextLine().trim().toLowerCase();
            if (ans.equals("y") || ans.equals("yes")) {
                Lead lead = leadService.createWithExistingContact(title, source, c);
                System.out.println("Лид создан: " + lead.getId() + ", статус NEW");
            } else {
                Lead lead = leadService.createWithNewContact(title, source, firstName, lastName, email, phone);
                System.out.println("Лид создан: " + lead.getId() + ", статус NEW");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Поле: " + e.getMessage());
        }

    }

    private void findLeadById() {
        System.out.print("ID лида: ");
        if (!scanner.hasNextLine()) {
            System.out.println("До свидания");
            System.exit(0);
        }
        String idStr = scanner.nextLine().trim();
        UUID id = parseUuid(idStr);
        if (id == null) {
            return;
        }
        Optional<Lead> found = leadService.findById(id);
        if (found.isEmpty()) {
            System.out.println("Лид с ID " + id + " не найден");
            return;
        }
        Lead lead = found.get();
        System.out.println("id: " + lead.getId());
        System.out.println("title: " + lead.getTitle());
        System.out.println("source: " + lead.getSource());
        System.out.println("status: " + lead.getStatus());
        if (lead.getContact() != null) {
            Contact c = lead.getContact();
            System.out.println("contact: " + c.getFirstName() + " " + c.getLastName() + ", " + (c.getEmail() == null ? "" : c.getEmail()) + ", " + (c.getPhone() == null ? "" : c.getPhone()));
        }
    }

    private void listLeads() {
        var leads = leadService.findAll();
        if (leads.isEmpty()) {
            System.out.println("Лидов не найдено");
            return;
        }
        System.out.println("id │ title │ source │ status");
        leads.stream()
                .map(l -> l.getId() + " │ " + l.getTitle() + " │ " + l.getSource() + " │ " + l.getStatus())
                .forEach(System.out::println);
    }

    private void deleteLead() {
        System.out.print("ID лида: ");
        if (!scanner.hasNextLine()) {
            System.out.println("До свидания");
            System.exit(0);
        }
        String idStr = scanner.nextLine().trim();
        UUID id = parseUuid(idStr);
        if (id == null) {
            return;
        }
        boolean removed = leadService.delete(id);
        if (removed) {
            System.out.println("Лид удалён");
        } else {
            System.out.println("Лид с ID " + id + " не найден");
        }
    }

    private UUID parseUuid(String idStr) {
        try {
            return UUID.fromString(idStr);
        } catch (Exception e) {
            System.out.println("Это не похоже на UUID");
            return null;
        }
    }

    private LeadSource readLeadSource() {
        while (true) {
            System.out.print("Источник (WEBSITE/CALL/PARTNER/REFERRAL): ");
            if (!scanner.hasNextLine()) {
                System.out.println("До свидания");
                System.exit(0);
            }
            String s = scanner.nextLine().trim();
            try {
                return LeadSource.valueOf(s);
            } catch (Exception e) {
                System.out.println("Неизвестное значение, допустимо: WEBSITE, CALL, PARTNER, REFERRAL");
            }
        }
    }

    private String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                System.out.println("До свидания");
                System.exit(0);
            }
            String s = scanner.nextLine().trim();
            if (!s.isEmpty()) {
                return s;
            }
            System.out.println("Поле не может быть пустым");
        }
    }

    private String readNullableLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println("До свидания");
            System.exit(0);
        }
        String s = scanner.nextLine().trim();
        return s.isEmpty() ? null : s;
    }

    private boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
