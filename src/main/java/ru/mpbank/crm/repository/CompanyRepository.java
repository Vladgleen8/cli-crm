package ru.mpbank.crm.repository;

import ru.mpbank.crm.domain.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends Repository<Company, UUID> {
    Optional<Company> findByInn(String inn);
}
