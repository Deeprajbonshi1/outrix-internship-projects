package com.Library.Librarymanagementsystem.service;

import com.Library.Librarymanagementsystem.entity.Book;
import com.Library.Librarymanagementsystem.exception.BookNotFoundException;
import com.Library.Librarymanagementsystem.exception.BookUnavailableException;
import com.Library.Librarymanagementsystem.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));
    }

    @Override
    public Book updateBook(Long id, Book updatedBook) {
        Book book = getBookById(id);
        book.setTitle(updatedBook.getTitle());
        book.setAuthor(updatedBook.getAuthor());
        book.setIsbn(updatedBook.getIsbn());
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Book issueBook(Long id, String issuedTo) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + id));

        if (book.isIssued()) {
            throw new BookUnavailableException("Book is already issued.");
        }

        book.setIssued(true);
        book.setIssuedTo(issuedTo);
        book.setIssueDate(LocalDate.now());

        return bookRepository.save(book);
    }


    @Override
    public Book returnBook(Long id) {
        Book book = getBookById(id);
        book.setIssued(false);
        book.setIssuedTo(null);
        book.setIssueDate(null);
        return bookRepository.save(book);
    }

    @Override
    public List<Book> searchBooks(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }
}