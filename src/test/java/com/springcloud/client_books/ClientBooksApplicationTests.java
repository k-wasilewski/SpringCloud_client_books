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
import static io.restassured.RestAssured.given;

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
    public void whenAccessProtectedResourceWithoutLogin_thenRedirectToLogin() throws Exception {
        Response response = RestAssured.get(ROOT_URI + "/book-service/books/1");

        given()
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .when()
                .redirects().follow(false)
                .get(ROOT_URI + "/book-service/books/1")
                .then()
                .statusCode(302)
                .header("Location", "http://localhost:8084/login");
    }

    @Test
    public void whenAccessProtectedResourceAfterLogin_thenSuccess() {
        Response response = RestAssured.given().auth()
                .form("user", "password", formConfig)
                .get(ROOT_URI + "/book-service/books/1");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void whenAddNewBook_thenSuccess() {
        RestAssured.defaultParser = Parser.JSON;

        Book book = new Book("How to spring cloud", "Baeldung");
        Response bookResponse = given().auth()
                .form("admin", "admin", formConfig).and()
                .header("Content-Type","application/json").and()
                .contentType(ContentType.JSON)
                .body(book)
                .post(ROOT_URI + "/book-service/books");
        Book result = bookResponse.as(Book.class);

        Assert.assertEquals(HttpStatus.OK.value(), bookResponse.getStatusCode());
        Assert.assertEquals(book.getAuthor(), result.getAuthor());
        Assert.assertEquals(book.getTitle(), result.getTitle());
    }

    @Test
    public void whenAdminAccessDiscoveryResource_thenSuccess() {
        Response response = RestAssured.given().auth()
                .form("admin", "admin", formConfig)
                .get(ROOT_URI + "/discovery");

        Assert.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }
}
