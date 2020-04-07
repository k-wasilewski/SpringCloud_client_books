package com.springcloud.client_books.controllers;

import com.google.gson.Gson;
import com.springcloud.client_books.entities.Book;
import com.springcloud.client_books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import com.springcloud.client_books.kafka.*;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private KafkaProducerConfig kafkaProducerConfig;
    private List<String> messageList= new ArrayList();

    @KafkaListener(topics = "post", groupId = "books")
    public void listenToMsgsPost(String message) {
        messageList.add("POST, "+message);
    }

    @KafkaListener(topics = "put", groupId = "books")
    public void listenToMsgsPut(String message) {
        messageList.add("PUT, "+message);
    }

    @KafkaListener(topics = "delete", groupId = "books")
    public void listenToMsgsDel(String message) {
        messageList.add("DEL, "+message);
    }

    @KafkaListener(topics = "patch", groupId = "books")
    public void listenToMsgsPatch(String message) {
        messageList.add("PATCH, "+message);
    }

    @GetMapping("/messages")
    public List<String> getMessageList() { return this.messageList;}

    @GetMapping
    public List<Book> findAllBooks() { return bookService.findAllBooks();}

    @GetMapping("/{bookId}")
    public Book findBook(@PathVariable Long bookId) {
        return bookService.findBookById(bookId);
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        Book newBook = bookService.createBook(book);
        kafkaProducerConfig.sendMessage(newBook.niceToString()+" created", "post");
        return newBook;
    }

    @DeleteMapping("/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        Book book = bookService.findBookById(bookId);
        kafkaProducerConfig.sendMessage(book.niceToString()+" deleted", "delete");
        bookService.deleteBook(bookId);
    }

    @PutMapping("/{bookId}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long bookId, Principal principal) {
        Book oldBook = bookService.findBookById(bookId);
        Book newBook = bookService.updateBook(book, bookId);
        kafkaProducerConfig.sendMessage(oldBook.niceToString()+" updated to "+newBook.niceToString(), "put");
        return newBook;
    }

    @PatchMapping("/{bookId}")
    public Book updateBook(@RequestBody Map<String, String> updates, @PathVariable Long bookId, Principal principal) {
        Book book = bookService.findBookById(bookId);
        kafkaProducerConfig.sendMessage(book.niceToString()+" updated with "+updates, "patch");
        updates.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(Book.class, k);
            ReflectionUtils.setField(field, book, v);
        });
        return bookService.saveBook(book);
    }
}
