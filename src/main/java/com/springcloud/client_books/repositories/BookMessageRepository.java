package com.springcloud.client_books.repositories;

import com.springcloud.client_books.entities.BookMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookMessageRepository extends JpaRepository<BookMessage, Long> {
    BookMessage getById(long id);
}