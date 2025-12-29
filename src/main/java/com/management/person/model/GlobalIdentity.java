package com.management.person.model;

import com.management.company.model.Company;
import jakarta.persistence.*;

@Entity
@Table(name = "global_identity", schema = "management")
public class GlobalIdentity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String document; // CPF buscado no login

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_company", nullable = false)
    private Company company;

    @Column(name = "fk_user_account", nullable = false)
    private Long fkUserAccount;

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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getFkUserAccount() {
        return fkUserAccount;
    }

    public void setFkUserAccount(Long fkUserAccount) {
        this.fkUserAccount = fkUserAccount;
    }
}
