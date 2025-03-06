package com.example.base_server.controller;

import com.example.base_server.client.GoogleBooksClient;
import com.example.base_server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private final GoogleBooksClient googleBooksClient;

    public BookController(GoogleBooksClient googleBooksClient) {
        this.googleBooksClient = googleBooksClient;
    }

    @PostMapping("/populate")
    public String searchBooks(@RequestParam String q) {
        return googleBooksClient.fetchBooksByCategory(q) ? "Database population succeed" : "Database population failed";
    }
}
