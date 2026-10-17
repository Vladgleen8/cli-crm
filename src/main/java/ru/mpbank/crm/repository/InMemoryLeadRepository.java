package ru.mpbank.crm.repository;

import ru.mpbank.crm.domain.Lead;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;


public class InMemoryLeadRepository implements LeadRepository {

    private final Map<UUID, Lead> storage = new HashMap<>();

    @Override
    public Lead save(Lead lead) {
        storage.put(lead.getId(), lead);
        return lead;
    }

    @Override
    public Optional<Lead> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Lead> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean deleteById(UUID id) {
        return storage.remove(id, storage.get(id));
    }

    @Override
    public Lead update(Lead lead) {
        if (!storage.containsKey(lead.getId())) {
            throw new IllegalArgumentException("Lead not found: " + lead.getId());
        }
        storage.put(lead.getId(), lead);
        return lead;

    }
}
