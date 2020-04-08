package com.springcloud.client_books.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="book_messages", schema = "cloud")
public class BookMessage {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private LocalDateTime dateTime;
    private String message;
    private String request;

    public BookMessage() {}

    public BookMessage(LocalDateTime dateTime, String request, String message) {
        this.dateTime=dateTime;
        this.request=request;
        this.message=message;
    }

    public long getId() {return this.id;}
    public void setId(long id) {this.id=id;}
    public LocalDateTime getDateTime() {return this.dateTime;}
    public void setDateTime(LocalDateTime dateTime) {this.dateTime=dateTime;}
    public String getMessage() {return this.message;}
    public void setMessage(String message) {this.message=message;}
    public String getRequest() {return this.request;}
    public void setRequest(String request) {this.request=request;}

    public String toString() {return "["+this.dateTime+"] "+this.request+": "+this.message;}
}