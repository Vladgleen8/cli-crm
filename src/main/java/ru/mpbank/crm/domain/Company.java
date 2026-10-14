package ru.mpbank.crm.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@ToString
public class Company {
    private final UUID id;
    private String name;
    private String inn;
    private String ogrn;
    private OwnershipForm form;
    private Address address;
}
