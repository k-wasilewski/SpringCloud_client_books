package com.springcloud.client_books;

import com.springcloud.client_books.entities.Book;
import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.config.RedirectConfig;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class ClientBooksApplicationTests {

    private final String ROOT_URI = "http://localhost:8084";
    private FormAuthConfig formConfig
            = new FormAuthConfig("/login", "username", "password");

    @Before
    public void setup() {
        RestAssured.config = RestAssured.config().redirect(
                RedirectConfig.redirectConfig().followRedirects(false));
    }

    @Test
    public void whenGetAllBooks_thenSuccess() {
        Response response = RestAssured.get(ROOT_URI + "/book-service/books");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void whenAccessProtectedResourceWithoutLogin_thenRedirectToLogin() {
        Response response = RestAssured.get(ROOT_URI + "/book-service/books/1");

        Assert.assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode());
        Assert.assertEquals("http://localhost:8084/login",
                response.getHeader("Location"));
    }

    @Test
    public void whenAddNewBook_thenSuccess() {
        RestAssured.defaultParser = Parser.JSON;

        Book book = new Book("How to spring cloud", "Baeldung");
        System.out.println(book);
        Response bookResponse = RestAssured.given().auth()
                .form("admin", "admin", formConfig).and()
                .contentType(ContentType.JSON)
                .body(book)
                .post(ROOT_URI + "/book-service/books");
        Book result = bookResponse.as(Book.class);
        System.out.println(result);

        Assert.assertEquals(HttpStatus.OK.value(), bookResponse.getStatusCode());
        Assert.assertEquals(book.getAuthor(), result.getAuthor());
        Assert.assertEquals(book.getTitle(), result.getTitle());
    }

}
