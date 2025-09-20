package com.project.construction.api.dto.response;

import java.math.BigDecimal;

public class PartnerResponse {
    private Long id;
    private String name;
    private String cpf;
    private BigDecimal ownershipPercentage;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public BigDecimal getOwnershipPercentage() {
        return ownershipPercentage;
    }

    public void setOwnershipPercentage(BigDecimal ownershipPercentage) {
        this.ownershipPercentage = ownershipPercentage;
    }
}
