package com.example.base_server.repository;

import com.example.base_server.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    //Dynamic search by title
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    //Dynamic search by author name
    Page<Book> findByAuthors_IdOrderByTitleAsc(Long id, Pageable pageable);

    //Dynamic search by username
    Page<Book> findByReadBy_IdOrderByTitleAsc(Long id, Pageable pageable);
}
