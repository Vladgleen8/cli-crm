package ru.mpbank.crm.app;

import ru.mpbank.crm.repository.ContactRepository;
import ru.mpbank.crm.repository.InMemoryContactRepository;
import ru.mpbank.crm.repository.InMemoryLeadRepository;
import ru.mpbank.crm.repository.LeadRepository;
import ru.mpbank.crm.service.LeadService;
import ru.mpbank.crm.view.LeadMenuView;

public class Main {

    public static void main(String[] args) {
        LeadRepository leadRepository = new InMemoryLeadRepository();
        ContactRepository contactRepository = new InMemoryContactRepository();
        LeadService leadService = new LeadService(leadRepository, contactRepository);
        LeadMenuView view = new LeadMenuView(leadService);
        view.run();
    }
}
