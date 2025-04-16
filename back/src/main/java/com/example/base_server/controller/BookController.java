package com.example.base_server.controller;

import com.example.base_server.dto.BookDTO;
import com.example.base_server.dto.UpdateBookDTO;
import com.example.base_server.enums.Role;
import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.service.AuthorService;
import com.example.base_server.service.BookService;
import com.example.base_server.utils.UserExtractor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

    // No public Create endpoint: books are created internally and cannot be added manually by users.

    //Get endpoints
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(new BookDTO(bookService.getBookById(id)));
    }

    @GetMapping
    public ResponseEntity<Page<BookDTO>> searchBookTitle(@RequestParam String title, Pageable pageable) {
        Page<BookDTO> results = bookService.getBookListByTitle(title, pageable).map(BookDTO::new);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<Page<BookDTO>> getAuthorBooks(@PathVariable Long id, Pageable pageable) {
        Page<BookDTO> results = bookService.getBookListByAuthor(id, pageable).map(BookDTO::new);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Page<BookDTO>> getUserReadBooks(@PathVariable Long id, Pageable pageable) {
        Page<BookDTO> results = bookService.getBookListByUser(id, pageable).map(BookDTO::new);
        return ResponseEntity.ok(results);
    }

    //Delete Book
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteBook(@PathVariable Long id, Authentication authentication) {
        if(UserExtractor.extractUser(authentication).getRole() != Role.ADMIN) {
            throw new BadCredentialsException("Only ADMIN can delete books");
        }

        return ResponseEntity.ok(bookService.deleteBook(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long id,
            @RequestBody UpdateBookDTO dto,
            Authentication authentication) {

        if(UserExtractor.extractUser(authentication).getRole() != Role.ADMIN) {
            throw new BadCredentialsException("Only ADMIN can update books");
        }

        Set<Author> authors = Set.of();
        if (dto.getAuthorIds() != null && !dto.getAuthorIds().isEmpty()) {
            authors = dto.getAuthorIds().stream()
                    .map(authorService::getAuthorById)
                    .collect(Collectors.toSet());
        }

        Book updated = bookService.updateBook(id, dto.getTitle(), authors);
        return ResponseEntity.ok(new BookDTO(updated));
    }
}
