package com.Library.Librarymanagementsystem.controller;

import com.Library.Librarymanagementsystem.entity.Book;
import com.Library.Librarymanagementsystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return "Book deleted successfully.";
    }

    @PostMapping("/{id}/issue")
    public Book issueBook(@PathVariable Long id, @RequestBody Map<String, String> request) {
        String issuedTo = request.get("issuedTo");
        if (issuedTo == null || issuedTo.isBlank()) {
            throw new IllegalArgumentException("issuedTo field is required");
        }
        return bookService.issueBook(id, issuedTo);
    }



    @PostMapping("/{id}/return")
    public Book returnBook(@PathVariable Long id) {
        return bookService.returnBook(id);
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String title) {
        return bookService.searchBooks(title);
    }
}