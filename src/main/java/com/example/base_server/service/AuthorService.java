package com.example.base_server.service;

import com.example.base_server.model.Author;
import com.example.base_server.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    //1- Find authors list by name.
    public List<Author> getAuthorsList(String name){
        return authorRepository.findByNameContaining(name);
    }

    @Transactional
    //2- Save New Author - If the author already exist, return it, if not, save it and return it.
    public Author saveAuthor(String name){
        Optional<Author> existingAuthor = authorRepository.findByNameContaining(name).stream().findFirst();
        return existingAuthor.orElseGet(() -> authorRepository.save(new Author(name)));
    }

    @Transactional
    //3- Update Author
    public boolean updateAuthor(String name, Long id){
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        author.setName(name);
        authorRepository.save(author);
        return true;
    }

    //4- Retrieve one Author by name
    public Optional<Author> getAuthorByName(String name){
        return authorRepository.findByNameContaining(name).stream().findFirst();
    }
}
