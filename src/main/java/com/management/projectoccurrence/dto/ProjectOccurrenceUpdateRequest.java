package com.management.projectoccurrence.dto;

import java.time.LocalDate;

public class ProjectOccurrenceUpdateRequest {

    private LocalDate occurrenceDate;
    private String description;

    // Getters and Setters
    public LocalDate getOccurrenceDate() {
        return occurrenceDate;
    }

    public void setOccurrenceDate(LocalDate occurrenceDate) {
        this.occurrenceDate = occurrenceDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
