
package com.hona.module_22.service;

import com.hona.module_22.dto.DocumentRequest;
import com.hona.module_22.dto.DocumentResponse;
import com.hona.module_22.entity.Document;
import com.hona.module_22.repository.DocumentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public DocumentResponse createDocument(DocumentRequest request) {
        Document document = new Document(
                request.getName(),
                request.getSize(),
                request.getStatus()
        );

        Document savedDocument = documentRepository.save(document);
        return toResponse(savedDocument);
    }

    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public DocumentResponse getDocumentById(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Document not found with id: " + id
                ));

        return toResponse(document);
    }

    @Transactional
    public DocumentResponse updateDocument(
            Long id, DocumentRequest request) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Document not found with id: " + id
                ));

        document.setName(request.getName());
        document.setSize(request.getSize());
        document.setStatus(request.getStatus());

        return toResponse(document);
    }

    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Document not found with id: " + id
            );
        }

        documentRepository.deleteById(id);
    }

    private DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getName(),
                document.getSize(),
                document.getStatus()
        );
    }
}
