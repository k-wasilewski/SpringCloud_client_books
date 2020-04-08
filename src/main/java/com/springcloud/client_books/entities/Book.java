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

    public Book() {}

    public Book(String titile, String author) {
        this.title=titile;
        this.author=author;
    }

    public long getId() {return this.id;}
    public void setId(long id) {this.id=id;}
    public String getTitle() {return this.title;}
    public void setTitle(String title) {this.title=title;}
    public String getAuthor() {return this.author;}
    public void setAuthor(String author) {this.author=author;}

    public String toString() {return "id="+this.id+"&title="+this.title+"&author="+this.author;}

    public String niceToString() {return "'"+this.title+"' by "+this.author;}
}
