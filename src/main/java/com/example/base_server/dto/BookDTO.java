package com.example.base_server.dto;

import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.model.KeyWord;

import java.util.List;
import java.util.stream.Collectors;

public class BookDTO {

    private Long id;
    private String title;
    private String authors;
    private String description;
    private String keyWords;

    public BookDTO(){}

    public BookDTO (Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.authors = formatAuthors(book.getAuthors());
        this.description = book.getDescription();
        this.keyWords = formatKeyWords(book.getKeyWords());
    }

    public static List<BookDTO> getBookDTOList(List<Book> books){
        return books.stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }

    //Util function for authors format
    private String formatAuthors(List<Author> authors) {
        if(authors == null || authors.isEmpty()){
            return "N/A";
        }
        return authors.stream()
                .map(Author::getName)
                .collect(Collectors.joining(", "));
    }

    //Util function for keywords format
    private String formatKeyWords(List<KeyWord> keyWords) {
        if(keyWords == null || keyWords.isEmpty()){
            return "N/A";
        }
        return keyWords.stream()
                .map(KeyWord::getValue)
                .collect(Collectors.joining(", "));
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", authors='" + authors + '\'' +
                ", description='" + description + '\'' +
                ", keyWords='" + keyWords + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public String getKeyWords() {
        return keyWords;
    }
}
