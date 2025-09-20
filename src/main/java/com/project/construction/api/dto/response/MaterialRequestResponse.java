package com.project.construction.api.dto.response;

public class MaterialRequestResponse {
    private Long id;
    private ConstructionSiteResponse constructionSite;
    private EmployeeResponse requestedBy;
    private String status;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ConstructionSiteResponse getConstructionSite() {
        return constructionSite;
    }

    public void setConstructionSite(ConstructionSiteResponse constructionSite) {
        this.constructionSite = constructionSite;
    }

    public EmployeeResponse getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(EmployeeResponse requestedBy) {
        this.requestedBy = requestedBy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
