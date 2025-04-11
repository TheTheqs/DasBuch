package com.example.base_server.repository;

import com.example.base_server.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    //Dynamic search by title
    List<Book> findByTitleContainingIgnoreCase(String title);

    //Dynamic search by author name
    Set<Book> findByAuthors_IdOrderByTitleAsc(Long id);

    //Dynamic search by username
    Set<Book> findByReadBy_IdOrderByTitleAsc(Long id);

}
