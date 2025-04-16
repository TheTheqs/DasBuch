package com.example.base_server.service;

import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.model.User;
import com.example.base_server.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {
    //Dependency Injection
    private final BookRepository bookRepository;
    private final AuthorService authorService;

    public BookService(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }
    //All CRUD methods
    //Create
    public Book createBook(String title, List<String> authorsNames) {

        Set<Author> authors = authorsNames.stream()
                .map(authorService::createAuthor)
                .collect(Collectors.toSet());

        List<Book> bookList = bookRepository.findByTitleContainingIgnoreCase(title.trim(), PageRequest.of(0, 10)).toList();

        Optional<Book> existingBook = bookList.stream()
                .filter(book -> book.getTitle().trim().equalsIgnoreCase(title.trim()) &&
                        book.getAuthors().equals(authors))
                .findFirst();

        return existingBook.orElseGet(() -> {
            Book newBook = new Book(title.trim(), authors, Set.of());
            return bookRepository.save(newBook);
        });

    }

    //Read
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found: " + id));
    }

    public Page<Book> getBookListByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Book> getBookListByAuthor(Long id, Pageable pageable) {
        return bookRepository.findByAuthors_IdOrderByTitleAsc(id, pageable);
    }

    public Page<Book> getBookListByUser(Long id, Pageable pageable) {
        return bookRepository.findByReadBy_IdOrderByTitleAsc(id, pageable);
    }

    //Update
    public Book updateBook(Long id, String title, Set<Author> authors) {
        Book relatedBook = bookRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Book not found!"));
        if(!title.trim().isEmpty()) {relatedBook.setTitle(title.trim());}
        if(authors != null  && !authors.isEmpty() && !authors.equals(relatedBook.getAuthors())) {
            relatedBook.setAuthors(authors);
        }
        return bookRepository.save(relatedBook);
    }

    //Delete
    public boolean deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            return false;
        }
        bookRepository.deleteById(id);
        return true;
    }
}
