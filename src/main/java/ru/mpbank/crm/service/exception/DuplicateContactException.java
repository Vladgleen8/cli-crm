package ru.mpbank.crm.service.exception;

import ru.mpbank.crm.domain.Contact;

public class DuplicateContactException extends RuntimeException {
    private final Contact existingContact;

    public DuplicateContactException(Contact existingContact) {
        super("Найден существующий контакт");
        this.existingContact = existingContact;
    }

    public Contact getExistingContact() {
        return existingContact;
    }
}
