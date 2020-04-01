package com.springcloud.client_books.entities;

import javax.persistence.*;

@Entity
@Table(name="books", schema = "cloud")
public class Book {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private String title;
    private String author;

    /*private String timestamp;
    private String message;
    private int status;
    private String error;
    private String path;*/

    public Book() {}

    public Book(String titile, String author) {
        this.title=titile;
        this.author=author;
    }

    /*public String getPath() {return this.path;}
    public void setPath(String path) {this.path=path;}
    public String getTimestamp() {return this.timestamp;}
    public void setTimestamp(String timestamp) {this.timestamp=timestamp;}
    public String getMessage() {return this.message;}
    public void setMessage(String message) {this.message=message;}
    public int getStatus() {return this.status;}
    public void setStatus(int status) {this.status=status;}
    public String getError() {return this.error;}
    public void setError(String error) {this.error=error;}*/

    public long getId() {return this.id;}
    public void setId(long id) {this.id=id;}
    public String getTitle() {return this.title;}
    public void setTitle(String title) {this.title=title;}
    public String getAuthor() {return this.author;}
    public void setAuthor(String author) {this.author=author;}

    public String toString() {return "id="+this.id+"&title="+this.title+"&author="+this.author;}
}
