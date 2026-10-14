package com.example.ticketdesk.address;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Address {

    @Id
    @GeneratedValue
    private Long id;

    private String district;
    private String buildingNumber;
    /** Short address code in the form "ABCD 1234". */
    private String shortCode;
    private String city;

    protected Address() {
    }

    public Address(String district, String buildingNumber, String shortCode, String city) {
        this.district = district;
        this.buildingNumber = buildingNumber;
        this.shortCode = shortCode;
        this.city = city;
    }

    public Long getId() { return id; }
    public String getDistrict() { return district; }
    public String getBuildingNumber() { return buildingNumber; }
    public String getShortCode() { return shortCode; }
    public String getCity() { return city; }
}
