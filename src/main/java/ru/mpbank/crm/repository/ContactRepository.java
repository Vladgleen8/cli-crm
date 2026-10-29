package ru.mpbank.crm.repository;

import ru.mpbank.crm.domain.Contact;

import java.util.Optional;
import java.util.UUID;

public interface ContactRepository extends Repository<Contact, UUID> {
    Optional<Contact> findByEmail(String email);
    Optional<Contact> findByPhone(String phone);
}
