package com.example.base_server.service;

import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.model.KeyWord;
import com.example.base_server.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private KeyWordService keyWordService;

    //1- Save new book
    public Book saveBook (String title, List<String> sAuthors, String description, List<String> sKeyWords, LocalDateTime publishedDate, String isbn, String coverURL, String externalLinks) {
        if(isbn == null || isbn.trim().isEmpty()){
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        Optional<Book> optionalBook = Optional.ofNullable(bookRepository.findByIsbn(isbn));
        if(optionalBook.isPresent()){
            return optionalBook.get();
        }
        List<Author> authors = sAuthors.stream()
                .map(authorService::saveAuthor)
                .toList();

        List<KeyWord> keyWords = sKeyWords.stream()
                .map(keyWordService::saveKeyWord)
                .toList();

        return bookRepository.save(new Book(title, authors, description, keyWords, publishedDate, isbn, coverURL, externalLinks));
    }
}
