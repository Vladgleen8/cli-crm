package ru.mpbank.crm.app;

import ru.mpbank.crm.domain.Lead;
import ru.mpbank.crm.domain.LeadSource;
import ru.mpbank.crm.domain.LeadStatus;
import ru.mpbank.crm.repository.InMemoryLeadRepository;
import ru.mpbank.crm.repository.LeadRepository;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {
        LeadRepository repository = new InMemoryLeadRepository();
        Lead credit = lead("Заявка на кредит", LeadSource.WEBSITE);
        repository.save(credit);
        repository.save(lead("Заявка на карту", LeadSource.CALL));
        repository.save(lead("Заявка на ипотеку", LeadSource.REFERRAL));

        System.out.println("--- findById ---");
        repository.findById(credit.getId()).ifPresent(found -> System.out.println(found.getTitle()));

        List<Lead> leads = repository.findAll();

        System.out.println("--- for ---");
        for (int i = 0; i < leads.size(); i++) {
            System.out.println(leads.get(i).getTitle());
        }

        System.out.println("--- for-each ---");
        for (Lead lead : leads) {
            System.out.println(lead.getTitle());
        }

        System.out.println("--- while ---");
        int index = 0;
        while (index < leads.size()) {
            System.out.println(leads.get(index).getTitle());
            index++;
        }

        System.out.println("--- do-while ---");
        int position = 0;
        do {
            System.out.println(leads.get(position).getTitle());
            position++;
        } while (position < leads.size());

        System.out.println("--- iterator ---");
        Iterator<Lead> iterator = leads.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().getTitle());
        }
    }

    private static Lead lead(String title, LeadSource source) {
        return Lead.builder()
                .id(UUID.randomUUID())
                .title(title)
                .source(source)
                .status(LeadStatus.DEFAULT)
                .createdAt(Instant.now())
                .build();
    }
}
