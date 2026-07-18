package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Add Book
    public Book addBook(Book book) {

        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new RuntimeException("Book with this ISBN already exists");
        }

        if (book.getAvailableCopies() <= 0 && book.getTotalCopies() > 0) {
            book.setAvailableCopies(book.getTotalCopies());
        }

        if (book.getTotalCopies() <= 0 && book.getAvailableCopies() > 0) {
            book.setTotalCopies(book.getAvailableCopies());
        }

        return bookRepository.save(book);
    }

    // Get All Books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get Book by ID
    public Book getBookById(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
    }

    // Update Book
    public Book updateBook(Long id, Book updatedBook) {

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));

        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        book.setCategory(updatedBook.getCategory());
        book.setDescription(updatedBook.getDescription());
        book.setImage(updatedBook.getImage());
        book.setPublisher(updatedBook.getPublisher());
        book.setLanguage(updatedBook.getLanguage());
        book.setPages(updatedBook.getPages());
        book.setPublishedYear(updatedBook.getPublishedYear());
        book.setTotalCopies(updatedBook.getTotalCopies());
        book.setAvailableCopies(updatedBook.getAvailableCopies());

        return bookRepository.save(book);
    }

    // Delete Book
    public void deleteBook(Long id) {

        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book Not Found");
        }

        bookRepository.deleteById(id);
    }
}