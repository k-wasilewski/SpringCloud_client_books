package com.springcloud.client_books.controllers;

import com.springcloud.client_books.entities.Book;
import com.springcloud.client_books.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "http://localhost:8084/login");
        httpServletResponse.setStatus(302);
        return "redirect:http://localhost:8084/login";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}