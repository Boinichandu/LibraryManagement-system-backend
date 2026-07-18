package com.example.demo.repository;

import com.example.demo.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find book by ISBN
    Optional<Book> findByIsbn(String isbn);

    // Find books by title
    List<Book> findByTitleContainingIgnoreCase(String title);

    // Find books by author
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // Find books by category
    List<Book> findByCategoryIgnoreCase(String category);

}
