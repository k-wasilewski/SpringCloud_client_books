package com.springcloud.client_books.controllers;

import com.google.gson.Gson;
import com.springcloud.client_books.entities.Book;
import com.springcloud.client_books.entities.BookMessage;
import com.springcloud.client_books.repositories.BookMessageRepository;
import com.springcloud.client_books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import com.springcloud.client_books.kafka.*;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;
    @Autowired
    private BookMessageRepository bookMessageRepository;
    private Map<LocalDateTime, String> messageList= new HashMap<>();

    @KafkaListener(topics = "post-books", groupId = "books")
    public void listenToMsgsPost(String message) {
        messageList.put(LocalDateTime.now(), "POST: "+message);
    }

    @KafkaListener(topics = "put-books", groupId = "books")
    public void listenToMsgsPut(String message) {
        messageList.put(LocalDateTime.now(), "PUT: "+message);
    }

    @KafkaListener(topics = "delete-books", groupId = "books")
    public void listenToMsgsDel(String message) {
        messageList.put(LocalDateTime.now(), "DEL: "+message);
    }

    @KafkaListener(topics = "patch-books", groupId = "books")
    public void listenToMsgsPatch(String message) {
        messageList.put(LocalDateTime.now(), "PATCH: "+message);
    }

    @GetMapping(value = "/messages", produces = {"application/json"})
    public Map<LocalDateTime, String> getMessageList() { return this.messageList;}

    @GetMapping
    public List<Book> findAllBooks() { return bookService.findAllBooks();}

    @GetMapping("/{bookId}")
    public Book findBook(@PathVariable Long bookId) {
        return bookService.findBookById(bookId);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        Book newBook = bookService.createBook(book);
        String message = newBook.niceToString()+" created";
        BookMessage dbMessage = new BookMessage(LocalDateTime.now(), "POST", message);
        bookMessageRepository.save(dbMessage);

        kafkaProducerConfig.sendMessage(newBook.niceToString()+" created", "post-books");
        return newBook;
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        Book book = bookService.findBookById(bookId);
        String message = book.niceToString()+" deleted";
        BookMessage dbMessage = new BookMessage(LocalDateTime.now(), "DELETE", message);
        bookMessageRepository.save(dbMessage);

        kafkaProducerConfig.sendMessage(message, "delete-books");
        bookService.deleteBook(bookId);
    }

    @PutMapping("/{bookId}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long bookId, Principal principal) {
        Book oldBook = bookService.findBookById(bookId);
        Book newBook = bookService.updateBook(book, bookId);
        String message = oldBook.niceToString()+" updated to "+newBook.niceToString();
        BookMessage dbMessage = new BookMessage(LocalDateTime.now(), "PUT", message);
        bookMessageRepository.save(dbMessage);

        kafkaProducerConfig.sendMessage(message, "put-books");
        return newBook;
    }

    @PatchMapping("/{bookId}")
    public Book updateBook(@RequestBody Map<String, String> updates, @PathVariable Long bookId, Principal principal) {
        Book book = bookService.findBookById(bookId);
        String message = book.niceToString()+" updated with "+updates;
        BookMessage dbMessage = new BookMessage(LocalDateTime.now(), "PATCH", message);
        bookMessageRepository.save(dbMessage);

        kafkaProducerConfig.sendMessage(message, "patch-books");
        updates.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Book.class, k);
            ReflectionUtils.setField(field, book, v);
        });
        return bookService.saveBook(book);
    }
}
