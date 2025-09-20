package com.project.construction.api.dto.request;

public class MaterialRequestRequest {
    private Long constructionSiteId;
    private Long requestedByEmployeeId;
    private String status;

    // Getters and setters
    public Long getConstructionSiteId() {
        return constructionSiteId;
    }

    public void setConstructionSiteId(Long constructionSiteId) {
        this.constructionSiteId = constructionSiteId;
    }

    public Long getRequestedByEmployeeId() {
        return requestedByEmployeeId;
    }

    public void setRequestedByEmployeeId(Long requestedByEmployeeId) {
        this.requestedByEmployeeId = requestedByEmployeeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
