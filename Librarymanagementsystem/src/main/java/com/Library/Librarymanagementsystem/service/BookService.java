package com.Library.Librarymanagementsystem.service;

import com.Library.Librarymanagementsystem.entity.Book;

import java.util.List;

public interface BookService {
    Book addBook(Book book);
    List<Book> getAllBooks();
    Book getBookById(Long id);
    Book updateBook(Long id, Book book);
    void deleteBook(Long id);
    Book issueBook(Long id, String issuedTo);
    Book returnBook(Long id);
    List<Book> searchBooks(String title);
}