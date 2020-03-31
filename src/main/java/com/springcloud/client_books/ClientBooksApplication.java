package com.springcloud.client_books;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@EnableJpaRepositories("com.springcloud.client_books.repositories")
public class ClientBooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientBooksApplication.class, args);
    }

}
