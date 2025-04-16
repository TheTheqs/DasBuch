package com.example.base_server.service;

import com.example.base_server.model.Author;
import com.example.base_server.model.Book;
import com.example.base_server.model.User;
import com.example.base_server.repository.BookRepository;
import org.springframework.data.domain.Page;
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
    public Book createBook(String title, List<String> authorNames, Pageable pageable) {

        Set<Author> authors = authorNames.stream()
                .map(authorService::createAuthor)
                .collect(Collectors.toSet());

        Page<Book> bookList = bookRepository.findByTitleContainingIgnoreCase(title.trim(), pageable);

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

    public Page<Book> getBookListByAuthor(Author author, Pageable pageable) {
        return bookRepository.findByAuthors_IdOrderByTitleAsc(author.getId(), pageable);
    }

    public Page<Book> getBookListByUser(User user, Pageable pageable) {
        return bookRepository.findByReadBy_IdOrderByTitleAsc(user.getId(), pageable);
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
    public boolean deleteBook(Book book) {
        if (!bookRepository.existsById(book.getId())) {
            return false;
        }
        bookRepository.delete(book);
        return true;
    }
}
