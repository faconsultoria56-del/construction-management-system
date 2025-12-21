package com.management.persondocument.model;

import com.management.documenttype.model.DocumentType;
import com.management.person.model.Person;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "person_document", schema = "management",
       uniqueConstraints = @UniqueConstraint(columnNames = {"fk_person", "fk_document_type"}))
@Data
@NoArgsConstructor
public class PersonDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_person", nullable = false)
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_value", nullable = false)
    private String documentValue;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PersonDocument(Person person, DocumentType documentType, String documentValue) {
        this.person = person;
        this.documentType = documentType;
        this.documentValue = documentValue;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
