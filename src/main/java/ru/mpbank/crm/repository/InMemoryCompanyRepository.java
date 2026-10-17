package ru.mpbank.crm.repository;

import ru.mpbank.crm.domain.Company;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.HashMap;
import java.util.UUID;

public class InMemoryCompanyRepository implements CompanyRepository {

    private final Map<UUID, Company> storage = new HashMap<>();

    @Override
    public Company save(Company company) {
        storage.put(company.getId(), company);
        return company;
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Company> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(UUID id) {
        return storage.remove(id, storage.get(id));
    }

    @Override
    public Company update(Company company) {
        if (!storage.containsKey(company.getId())) {
            throw new IllegalArgumentException("Company not found: " + company.getId());
        }
        storage.put(company.getId(), company);
        return company;

    }
}