package com.springcloud.client_books.repositories;

import com.springcloud.client_books.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findAll();

    Optional<Book> findById(Long bookId);

    void deleteById(Long bookId);

}
