package com.orchid.orchid_marketplace.dto;

import java.util.UUID;

public class ShippingOptionResponse {
    private UUID id;
    private String name;
    private Double price;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}
