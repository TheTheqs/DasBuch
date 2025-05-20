package com.example.base_server.controller;

import com.example.base_server.model.RecoverEmailRequest;
import com.example.base_server.service.RecoverEmailRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recovery")
public class RecoverEmailRequestController {

    private final RecoverEmailRequestService service;

    @Autowired
    public RecoverEmailRequestController(RecoverEmailRequestService service) {
        this.service = service;
    }

    // 1. Criar nova requisição
    @PostMapping
    public ResponseEntity<RecoverEmailRequest> createRequest(@RequestParam String email) {
        RecoverEmailRequest request = service.createRequest(email, "test token");
        return ResponseEntity.ok(request);
    }

    // 2. Buscar todas as requisições
    @GetMapping
    public ResponseEntity<List<RecoverEmailRequest>> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    // 3. Buscar não resolvidas
    @GetMapping("/pending")
    public ResponseEntity<List<RecoverEmailRequest>> getUnresolvedRequests() {
        return ResponseEntity.ok(service.getUnresolvedRequests());
    }

    // 4. Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<RecoverEmailRequest> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Atualizar status de resolved
    @PutMapping("/{id}/resolve")
    public ResponseEntity<RecoverEmailRequest> updateResolvedStatus(
            @PathVariable Long id,
            @RequestParam boolean resolved
    ) {
        return service.updateResolvedStatus(id, resolved)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 6. Deletar requisição
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        service.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
