
package com.hona.module_22.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DocumentRequest {

    @NotBlank(message = "Document name is required")
    private String name;

    @NotNull(message = "Document size is required")
    @Positive(message = "Document size must be greater than zero")
    private Long size;

    @NotBlank(message = "Document status is required")
    private String status;

    public DocumentRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
