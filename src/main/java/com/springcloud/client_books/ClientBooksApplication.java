package com.springcloud.client_books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaRepositories("com.springcloud.client_books.repositories")
public class ClientBooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientBooksApplication.class, args);
    }

}
