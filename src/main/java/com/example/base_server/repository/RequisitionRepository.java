package com.example.base_server.repository;

import com.example.base_server.model.Requisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequisitionRepository extends JpaRepository <Requisition, Long> {

    //Get the oldest requisition in the database.
    @Query("SELECT r FROM Requisition r WHERE r.id = (SELECT MIN(r2.id) FROM Requisition r2)")
    Optional<Requisition> findOldestRequisition();

    //Get all requisitions by user
    List<Requisition> findByUserId(Long userId);

    //Get the total amount of requisitions by a user
    @Query("SELECT COUNT(r) FROM Requisition r WHERE r.user.id = :userId")
    int countByUserId(Long userId);

}
