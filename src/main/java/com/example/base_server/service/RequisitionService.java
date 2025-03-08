package com.example.base_server.service;

import com.example.base_server.model.Requisition;
import com.example.base_server.model.User;
import com.example.base_server.repository.RequisitionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequisitionService {

    private final RequisitionRepository requisitionRepository;

    @Autowired
    public RequisitionService(RequisitionRepository requisitionRepository) {
        this.requisitionRepository = requisitionRepository;
    }

    //1- Is there any requisition to be attended?
    public boolean haveRequisition(){
        return requisitionRepository.existsById(1L);
    }

    //2- Get the oldest requisition in database.
    public Requisition getOldestRequisition() {
        return requisitionRepository.findOldestRequisition()
                .orElseThrow(() -> new EntityNotFoundException("No requisitions found"));
    }

    //3- Get all requisitions from a user
    public List<Requisition> getAllRequisitionsByUserId(Long id){
        return requisitionRepository.findByUserId(id);
    }

    //4- Save new Requisition
    public Requisition saveRequisition(String title, String author, User user) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("Author cannot be null or empty");
        }

        Requisition requisition = new Requisition(title, author, user);
        return requisitionRepository.save(requisition);
    }

    //5- Delete requisition by ID
    public void deleteRequisition(Requisition requisition){
        requisitionRepository.deleteById(requisition.getId());
    }
}
