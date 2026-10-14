package com.example.ticketdesk.address;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/** Seeds fake addresses on startup. None of this data is real. */
@Component
public class AddressSeeder implements CommandLineRunner {

    private static final String[] DISTRICTS = {
            "North Hills", "Palm Gardens", "River Side", "Old Market", "Green Valley", "Sunset Park"
    };
    private static final String[] CITIES = {"Sample City", "Demo Town"};
    private static final String[] CODE_PREFIXES = {"ABCD", "ABCE", "KLMN", "PQRS", "WXYZ"};

    private final AddressRepository repository;

    public AddressSeeder(AddressRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }
        List<Address> addresses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            String district = DISTRICTS[i % DISTRICTS.length];
            String building = String.valueOf(1000 + i * 37);
            String code = CODE_PREFIXES[i % CODE_PREFIXES.length] + " " + (1200 + i * 11);
            addresses.add(new Address(district, building, code, CITIES[i % CITIES.length]));
        }
        repository.saveAll(addresses);
    }
}
