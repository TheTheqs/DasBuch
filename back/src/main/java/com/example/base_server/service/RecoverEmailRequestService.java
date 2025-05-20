package com.example.base_server.service;

import com.example.base_server.model.RecoverEmailRequest;
import com.example.base_server.repository.RecoverEmailRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecoverEmailRequestService {

    private final RecoverEmailRequestRepository repository;

    @Autowired
    public RecoverEmailRequestService(RecoverEmailRequestRepository repository) {
        this.repository = repository;
    }

    // 1. Criar nova requisição
    public RecoverEmailRequest createRequest(String email, String token) {
        RecoverEmailRequest request = new RecoverEmailRequest(email, token, false);
        return repository.save(request);
    }

    // 2. Buscar todas
    public List<RecoverEmailRequest> getAllRequests() {
        return repository.findAll();
    }

    // 3. Buscar somente as não resolvidas
    public List<RecoverEmailRequest> getUnresolvedRequests() {
        return repository.findAllByResolvedFalseOrderByCreatedAtDesc();
    }

    // 4. Buscar por ID
    public Optional<RecoverEmailRequest> getById(Long id) {
        return repository.findById(id);
    }

    // 5. Atualizar status de resolved
    public Optional<RecoverEmailRequest> updateResolvedStatus(Long id, boolean resolved) {
        Optional<RecoverEmailRequest> optionalRequest = repository.findById(id);
        if (optionalRequest.isPresent()) {
            RecoverEmailRequest request = optionalRequest.get();
            request.setResolved(resolved);
            return Optional.of(repository.save(request));
        }
        return Optional.empty();
    }

    // 6. Deletar requisição
    public void deleteRequest(Long id) {
        repository.deleteById(id);
    }
}
