package ru.mpbank.crm.repository;

import ru.mpbank.crm.domain.Company;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;

public class InMemoryCompanyRepository implements CompanyRepository {

    private final Map<UUID, Company> storageById = new HashMap<>();
    private final Map<String, Company> storageByInn = new HashMap<>();

    public Company findOrCreate(String name, String rawInn) {
        String key = normalizeInn(rawInn);
        Company existing = storageByInn.get(key);
        if (existing != null) {
            return existing;
        }
        Company created = Company.builder().id(UUID.randomUUID()).name(name).inn(key).build();
        storageById.put(created.getId(), created);
        storageByInn.put(key, created);
        return created;
    }

    static String normalizeInn(String raw) {
        String digits = raw.replaceAll("\\D", "");   // выкидываем всё, кроме цифр
        if (digits.length() != 10 && digits.length() != 12) {
            throw new IllegalArgumentException("ИНН должен содержать 10 или 12 цифр: " + raw);
        }
        return digits;
    }

    @Override
    public Company save(Company company) {
        storageById.put(company.getId(), company);
        return company;
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return Optional.ofNullable(storageById.get(id));
    }

    @Override
    public List<Company> findAll() {
        return new ArrayList<>(storageById.values());
    }

    @Override
    public boolean deleteById(UUID id) {
        return storageById.remove(id, storageById.get(id));
    }

    @Override
    public Company update(Company company) {
        if (!storageById.containsKey(company.getId())) {
            throw new IllegalArgumentException("Company not found: " + company.getId());
        }
        storageById.put(company.getId(), company);
        return company;

    }
}