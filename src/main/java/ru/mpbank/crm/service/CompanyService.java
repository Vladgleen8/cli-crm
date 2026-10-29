package ru.mpbank.crm.service;

import ru.mpbank.crm.domain.Company;
import ru.mpbank.crm.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CompanyService {

    private final CompanyRepository repository;

    public CompanyService(CompanyRepository repository) {
        this.repository = repository;
    }

    public Company create(String name, String inn) {
        Optional<Company> existing = repository.findByInn(inn);
        if (existing.isPresent()) {
            return existing.get();
        }
        Company company = Company.builder()
                .id(UUID.randomUUID())
                .name(name)
                .inn(inn)
                .build();
        return repository.save(company);
    }

    public Optional<Company> findById(UUID id) {
        return repository.findById(id);
    }

    public List<Company> findAll() {
        return repository.findAll();
    }

    public boolean delete(UUID id) {
        return repository.deleteById(id);
    }
}