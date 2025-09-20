package com.project.construction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MaterialApprovalException extends RuntimeException {
    public MaterialApprovalException(String message) {
        super(message);
    }
}
