package com.example.base_server.controller;

import com.example.base_server.client.GoogleBooksClient;
import com.example.base_server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    GoogleBooksClient googleBooksClient;

    @PostMapping("/populate")
    public String populate() {
        return googleBooksClient.fetchBooksByCategory() ? "Populate succeeded" : "Populate failed";
    }
}
