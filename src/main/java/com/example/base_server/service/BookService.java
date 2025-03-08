package com.example.base_server.service;

import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.model.KeyWord;
import com.example.base_server.repository.BookRepository;
import com.example.base_server.utils.TxtFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            writeLog("The book is already in database, retrieving it... \n" + optionalBook.get().getTitle());
            return optionalBook.get();
        }
        for (String name: sAuthors){
            if (name.length() > 255) {
                System.out.println(name);
                name = name.substring(0, 255);
                System.out.println(name);
            }
        }
        List<Author> authors = sAuthors.stream()
                .map(authorService::saveAuthor)
                .toList();

        List<KeyWord> keyWords = sKeyWords.stream()
                .map(keyWordService::saveKeyWord)
                .toList();
        Book newBook = bookRepository.save(new Book(title, authors, description, keyWords, publishedDate, isbn, coverURL, externalLinks));
        writeLog("New book saved in database, retrieving it... \n" + newBook.getTitle());
        return newBook;
    }
    //2- Create log messages
    private void writeLog(String message) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        TxtFileUtil.write("book-service-log.txt", false, "[" + timestamp + "] " + message);
    }

    //3- Get total books in database
    public Long getTotalBooksNumber(){
        return bookRepository.count();
    }

}
