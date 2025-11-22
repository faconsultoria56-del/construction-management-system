package com.management.company.model;

import com.management.address.model.Address;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "company", schema = "management")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String document;

    @Column(name = "registered_name")
    private String registeredName;

    @Column(name = "trade_name")
    private String tradeName;

    @Column(name = "registration_status_description")
    private String registrationStatusDescription;

    @Column(name = "primary_phone")
    private String primaryPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_address")
    private Address address;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getRegisteredName() {
        return registeredName;
    }

    public void setRegisteredName(String registeredName) {
        this.registeredName = registeredName;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getRegistrationStatusDescription() {
        return registrationStatusDescription;
    }

    public void setRegistrationStatusDescription(String registrationStatusDescription) {
        this.registrationStatusDescription = registrationStatusDescription;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
