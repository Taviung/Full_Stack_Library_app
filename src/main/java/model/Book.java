package model;
import java.time.LocalDate;
import java.util.Date;

public class Book{
    private Long id;
    private String author;
    private String title;
    private LocalDate publishedDate;
    private int stock;
    private double price;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public LocalDate getPublishedDate() {
        return publishedDate;
    }
    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }
    public void setStock(int stock) {this.stock = stock;}
    public int getStock() {return stock;}
    public void setPrice(double price) {this.price = price;}
    public double getPrice() {return price;}

    @Override
    public String toString(){
        return "Book: ID: " + id + " Title: " + title + " Author: " + author + " Published Date: " + publishedDate;

    }
}