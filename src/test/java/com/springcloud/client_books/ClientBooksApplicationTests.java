package com.springcloud.client_books;

import com.springcloud.client_books.controllers.BookController;
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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.*;
import org.springframework.test.context.junit4.*;
import org.springframework.test.context.web.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

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
    public void whenAddNewBook_thenSuccess() {
        RestAssured.defaultParser = Parser.JSON;

        Book book = new Book("How to spring cloud", "Baeldung");
        System.out.println("1"+book);
        Response bookResponse = given().auth()
                .form("admin", "admin", formConfig).and()
                .header("Content-Type","application/json").and()
                .contentType(ContentType.JSON)
                .body(book)
                .post(ROOT_URI + "/book-service/books");
        System.out.println("2"+bookResponse);
        String result = bookResponse.getBody().asString();
        //Book result2 = bookResponse.as(Book.class);
        System.out.println("3"+result);

        Assert.assertEquals(HttpStatus.OK.value(), bookResponse.getStatusCode());
        //Assert.assertEquals(book.getAuthor(), result.getAuthor());
        //Assert.assertEquals(book.getTitle(), result.getTitle());
    }

    public static String mapToString(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value="";
            if (map.get(key).getClass().isAssignableFrom(Integer.class)) value = map.get(key).toString();
            else value = (String) map.get(key);
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return stringBuilder.toString();
    }

}
