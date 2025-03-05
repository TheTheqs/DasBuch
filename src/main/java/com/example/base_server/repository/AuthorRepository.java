package com.example.base_server.repository;

import com.example.base_server.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByName(String name);

    //The following method returns a list with authors which name contains the argument
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Author> findByNameContaining(String name);

    //The following method will assure that an author always comes with its books.
    @Query("SELECT a FROM Author a LEFT JOIN FETCH a.books WHERE a.name = :name")
    Author findByNameWithBooks(@Param("name") String name);
}
