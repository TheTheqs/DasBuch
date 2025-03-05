package com.example.base_server.repository;

import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.model.KeyWord;
import com.example.base_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    //Find by ISBN, another unique attribute
    Book findByIsbn(String isbn);

    //The following method returns a list with books which titles contains the argument
    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Book> findByTitleContaining(@Param("title") String title);

    //The following method returns a list with books which contains the provided author
    @Query("SELECT b FROM Book b JOIN b.authors a WHERE a = :author")
    List<Book> findByAuthor(@Param("author") Author author);

    //The following method returns a list with books which contains the provided keyword
    @Query("SELECT b FROM Book b JOIN b.keyWords k WHERE k = :keyword")
    List<Book> findByKeyword(@Param("keyword") KeyWord keyword);

    //The following method returns a list with books which contains the provided user
    @Query("SELECT b FROM Book b JOIN b.readBy u WHERE u = :user")
    List<Book> findByUser(@Param("user") User user);
}
