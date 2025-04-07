package com.example.base_server.repository;

import com.example.base_server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Checks if the given email already exists in the database.
     * @param email The email to check.
     * @return true if the email is already registered.
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves a User by their email.
     * @param email The email of the user.
     * @return The User with the provided email, or null if not found.
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a User by their verification token.
     * Used in the email validation system.
     * @param token The verification token.
     * @return The User associated with the provided token, or null if not found.
     */
    Optional<User>  findByVerificationToken(String token);

    /**
     * Retrieves a User by their reset token.
     * Used in the password reset system.
     * @param token The reset token.
     * @return The User associated with the provided token, or null if not found.
     */
    Optional<User>  findByResetToken(String token);
}

// Standard CRUD methods provided by JpaRepository:
// No need to implement them manually, they are inherited from JpaRepository.

// userRepository.save(user);
// Saves a new User or updates an existing one.

// userRepository.findById(1L);
// Retrieves a User by their ID. Returns an Optional<User> to avoid NullPointerException.

// userRepository.findAll();
// Retrieves a list of all Users.

// userRepository.existsById(1L);
// Checks if a User with the given ID exists in the database.

// userRepository.count();
// Returns the total number of Users in the database.

// userRepository.deleteById(1L);
// Deletes a User by their ID.

// userRepository.delete(user);
// Deletes the given User entity.

// userRepository.deleteAll();
// Deletes all Users from the database. Be careful when using this!

