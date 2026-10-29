package ru.mpbank.crm.repository;

import ru.mpbank.crm.domain.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


public class InMemoryContactRepository implements ContactRepository {

    private final Map<UUID, Contact> storage = new HashMap<>();
    private final Map<String, Contact> byEmail = new HashMap<>();
    private final Map<String, Contact> byPhone = new HashMap<>();

    @Override
    public Contact save(Contact contact) {
        String emailKey = contact.getEmail() == null
                ? null
                : normalizeEmail(contact.getEmail());
        String phoneKey = contact.getPhone() == null
                ? null
                : normalizePhoneNumber(contact.getPhone());

        if (emailKey != null) {
            Contact existing = byEmail.get(emailKey);
            if (existing != null) {
                return existing;
            }
        }
        if (phoneKey != null) {
            Contact existing = byPhone.get(phoneKey);
            if (existing != null) {
                return existing;
            }
        }

        storage.put(contact.getId(), contact);
        if (emailKey != null) {
            byEmail.put(emailKey, contact);
        }
        if (phoneKey != null) {
            byPhone.put(phoneKey, contact);
        }
        return contact;
    }

    @Override
    public Optional<Contact> findById(UUID uuid) {
        return Optional.ofNullable(storage.get(uuid));
    }

    @Override
    public List<Contact> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(UUID id) {
        return storage.remove(id, storage.get(id));
    }

    @Override
    public Contact update(Contact contact) {
        if (!storage.containsKey(contact.getId())) {
            throw new IllegalArgumentException("Contact not found: " + contact.getId());
        }
        storage.put(contact.getId(), contact);
        return contact;
    }

    @Override
    public Optional<Contact> findByEmail(String rawEmail) {
        String emailKey = normalizeEmail(rawEmail);
        return Optional.ofNullable(byEmail.get(emailKey));
    }

    @Override
    public Optional<Contact> findByPhone(String rawPhone) {
        String phone = normalizePhoneNumber(rawPhone);
        return Optional.ofNullable(byPhone.get(phone));
    }

    public String normalizeEmail(String rawEmail) {
        return rawEmail.trim().toLowerCase();
    }

    public String normalizePhoneNumber(String rawPhoneNumber) {
        String digits = rawPhoneNumber.replaceAll("\\D", "");

        if (digits.length() == 11 && digits.startsWith("8")) {
            digits = "7" + digits.substring(1);
        } else if (digits.length() == 10) {
            digits = "7" + digits;
        }

        if (digits.length() != 11 || !digits.startsWith("7")) {
            throw new IllegalArgumentException("Некорректный номер: " + rawPhoneNumber);
        }

        return "+" + digits;
    }
}
