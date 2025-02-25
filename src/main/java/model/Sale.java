package model;

import java.time.LocalDate;
import java.util.Date;

public class Sale {
    private long id;
    private String bookTitle;
    private int stock;
    private double price;
    private LocalDate selldate;
    private String seller;

    public long getId() {
        return id;
    }

    public double getTotalPrice() {
        return price;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public int getQuantity() {
        return stock;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTotalPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int stock) {
        this.stock = stock;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public LocalDate getSelldate() {
        return selldate;
    }

    public void setSelldate(LocalDate selldate) {
        this.selldate = selldate;
    }

    @Override
    public String toString() {
        return "Id= " + id +" bookTitle= " + bookTitle + " stock= " + stock + " price= " + price;
    }
}
