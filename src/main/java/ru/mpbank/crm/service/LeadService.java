package ru.mpbank.crm.service;

import ru.mpbank.crm.domain.Contact;
import ru.mpbank.crm.domain.Lead;
import ru.mpbank.crm.domain.LeadSource;
import ru.mpbank.crm.domain.LeadStatus;
import ru.mpbank.crm.repository.ContactRepository;
import ru.mpbank.crm.repository.InMemoryContactRepository;
import ru.mpbank.crm.repository.LeadRepository;
import ru.mpbank.crm.service.dto.CreateLeadResult;
import ru.mpbank.crm.service.exception.DuplicateContactException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LeadService {

    private final LeadRepository leadRepository;
    private final ContactRepository contactRepository;

    public LeadService(LeadRepository leadRepository, ContactRepository contactRepository) {
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
    }

    public Lead create(String title,
                                        String source,
                                        String firstName,
                                        String lastName,
                                        String email,
                                        String phone) {
        if (email != null) {
            Optional<Contact> foundContactByEmail = contactRepository.findByEmail(email);
            if (foundContactByEmail.isPresent()) {
                throw new DuplicateContactException(foundContactByEmail.get());
            } else {
                contactRepository.save(foundContactByEmail.get());
            }
        }

        if (phone != null) {
            Optional<Contact> foundContactByPhone = contactRepository.findByPhone(phone);
            if (foundContactByPhone.isPresent()) {
                throw new DuplicateContactException(foundContactByPhone.get());
            }  else {
                contactRepository.save(foundContactByPhone.get());
            }
        }

        LeadSource leadSource = LeadSource.fromString(source);
        Contact contact = Contact.builder()
                .id(UUID.randomUUID())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .build();

        contact = contactRepository.save(contact);

        Lead lead = Lead.builder()
                .id(UUID.randomUUID())
                .title(title)
                .source(leadSource)
                .status(LeadStatus.NEW)
                .contact(contact)
                .createdAt(Instant.now())
                .build();
        return leadRepository.save(lead);
    }

    public Lead createWithNewContact(String title, LeadSource source,
                                     String firstName, String lastName,
                                     String email, String phone) {
        Contact contact = Contact.builder()
                .id(UUID.randomUUID())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .build();

        contact = contactRepository.save(contact);

        Lead lead = Lead.builder()
                .id(UUID.randomUUID())
                .title(title)
                .source(source)
                .status(LeadStatus.NEW)
                .contact(contact)
                .createdAt(Instant.now())
                .build();
        return leadRepository.save(lead);
    }


    public Lead createWithExistingContact(String title, LeadSource source,
                                     String firstName, String lastName,
                                     String email, String phone) {

    }

    public Optional<Lead> findById(UUID id) {
        return leadRepository.findById(id);
    }

    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    public boolean delete(UUID id) {
        return leadRepository.deleteById(id);
    }
}
