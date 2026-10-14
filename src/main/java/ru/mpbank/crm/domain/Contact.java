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
public class Contact {
    private final UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Address address;
}
