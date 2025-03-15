package com.example.base_server.controller;

import com.example.base_server.dto.BookDTO;
import com.example.base_server.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;


    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam String query) {
        List<BookDTO> books = BookDTO.getBookDTOList(bookService.getBooksContaining(query));
        return ResponseEntity.ok(books);
    }
}
