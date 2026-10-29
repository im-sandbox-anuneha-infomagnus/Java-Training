
package com.hona.module_22.dto;

public class DocumentResponse {

    private Long id;
    private String name;
    private Long size;
    private String status;

    public DocumentResponse() {
    }

    public DocumentResponse(Long id, String name, Long size, String status) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getSize() {
        return size;
    }

    public String getStatus() {
        return status;
    }
}
