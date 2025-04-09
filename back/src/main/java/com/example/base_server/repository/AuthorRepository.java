package com.example.base_server.repository;

import com.example.base_server.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    //Custom method to get author by name
    Optional<Author> findByName(String name);

    //General method to search for authors dynamically
    List<Author> findByNameContainingIgnoreCase(String name);

    //Exist author for short verifications
    boolean existsByName(String name);
}
