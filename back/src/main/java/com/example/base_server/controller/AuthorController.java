package com.example.base_server.controller;

import com.example.base_server.dto.AuthorDTO;
import com.example.base_server.dto.UpdateAuthorDTO;
import com.example.base_server.enums.Role;
import com.example.base_server.model.Author;
import com.example.base_server.service.AuthorService;
import com.example.base_server.utils.UserExtractor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // No public Create endpoint: authors are created internally and cannot be added manually by users.

    //Get authors
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthor(@PathVariable Long id) {
        return ResponseEntity.ok(new AuthorDTO(authorService.getAuthorById(id)));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AuthorDTO>> searchAuthorName(@RequestParam String name, Pageable pageable) {
        Page<AuthorDTO> results = authorService.getAuthorListByName(name, pageable).map(AuthorDTO::new);
        return ResponseEntity.ok(results);
    }
    //Delete authors
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteAuthor(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(authorService.deleteAuthor(id));
    }

    //Update author
    @PatchMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(
            @PathVariable Long id,
            @RequestBody UpdateAuthorDTO dto,
            Authentication authentication) {
        Author updated = authorService.updateAuthor(id, dto.getName());
        return ResponseEntity.ok(new AuthorDTO(updated));
    }

}
