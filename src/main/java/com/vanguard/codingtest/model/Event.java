package com.vanguard.codingtest.model;

import jakarta.persistence.Entity;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class Event extends AbstractPersistable<Long> {
    private String sellerParty;
    private String buyerParty;
    private String premiumCurrency;
    private double premiumAmount;

    public Event() {
        this(null);
    }

    protected Event(Long id) {
        this.setId(id);
    }

    public Event(String sellerParty, String buyerParty, String premiumCurrency, double premiumAmount) {
        this.sellerParty = sellerParty;
        this.buyerParty = buyerParty;
        this.premiumCurrency = premiumCurrency;
        this.premiumAmount = premiumAmount;
    }

    public Event(String sellerParty, String buyerParty, String premiumCurrency, double premiumAmount, Long id) {
        this.setId(id);
        this.sellerParty = sellerParty;
        this.buyerParty = buyerParty;
        this.premiumCurrency = premiumCurrency;
        this.premiumAmount = premiumAmount;
    }

    public String getSellerParty() {
        return sellerParty;
    }

    public String getBuyerParty() {
        return buyerParty;
    }

    public String getPremiumCurrency() {
        return premiumCurrency;
    }

    public double getPremiumAmount() {
        return premiumAmount;
    }
}
