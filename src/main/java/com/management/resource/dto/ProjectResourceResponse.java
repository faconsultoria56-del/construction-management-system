package com.management.resource.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProjectResourceResponse {
    private Integer id;
    private Integer projectId;
    private String name;
    private String unit;
    private BigDecimal quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
