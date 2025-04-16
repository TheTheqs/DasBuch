package com.example.base_server.repository;

import com.example.base_server.model.Author;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    //Custom method to get author by name
    Optional<Author> findByName(String name);

    //General method to search for authors dynamically
    Page<Author> findByNameContainingIgnoreCase(String name, Pageable pageable);

    //Exist author for short verifications
    boolean existsByName(String name);
}
