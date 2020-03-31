package com.springcloud.client_books.services;

import com.springcloud.client_books.entities.Book;
import com.springcloud.client_books.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book saveBook(Book book) { return bookRepository.save(book); }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);

        if (book.get()!=null) return book.get();
        else return null;
    }

    public Book createBook(Book book) {return bookRepository.save(book);}

    public void deleteBook(Long bookId) {bookRepository.deleteById(bookId);}

    public Book updateBook(Book book, Long bookId) {
        book.setId(bookId);
        return bookRepository.save(book);
    }
}
