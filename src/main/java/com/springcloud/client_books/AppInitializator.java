package com.springcloud.client_books;

import com.springcloud.client_books.controllers.BookController;
import com.springcloud.client_books.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * for testing purposes
 */
@Component
class AppInitializator {
    @Autowired
    BookController bookController;

    @PostConstruct
    private void init() {
        bookController.createBook(new Book("666", "666"));
    }
}
