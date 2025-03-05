package com.example.base_server.repository;

import com.example.base_server.model.KeyWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KeyWordRepository extends JpaRepository <KeyWord, Long> {

    boolean existsByValue(String value);

    KeyWord getByValue(String value);

    //The following method will assure that keywords always comes with its books.
    @Query("SELECT k FROM KeyWord k LEFT JOIN FETCH k.books WHERE k.value = :value")
    KeyWord findByValueWithBooks(@Param("value") String value);
}
