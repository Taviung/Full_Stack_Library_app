package view.model;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Date;

public class SaleDTO {
    private StringProperty bookTitle;
    private IntegerProperty quantity;
    private DoubleProperty price;
    private StringProperty seller;
    private ObjectPropertyBase<LocalDate> sellDate;

    public void setSellDate(LocalDate date){ sellDateProperty().set(date); }
    public LocalDate getSellDate() { return sellDateProperty().get(); }
    public ObjectPropertyBase<LocalDate> sellDateProperty() {
        if(sellDate == null) {
            sellDate=new SimpleObjectProperty<LocalDate>();
        }
        return sellDate;
    }

    public void setBookTitle(String bookTitle) {
        bookTitleProperty().set(bookTitle);
    }
    public String getBookTitle() {
        return bookTitleProperty().get();
    }
    public StringProperty bookTitleProperty() {
        if (bookTitle == null) {
            bookTitle = new SimpleStringProperty();
        }
        return bookTitle;
    }

    public void setSeller(String seller) {
        sellerProperty().set(seller);
    }
    public String getSeller() {
        return sellerProperty().get();
    }
    public StringProperty sellerProperty() {
        if (seller == null) {
            seller = new SimpleStringProperty();
        }
        return seller;
    }

    public void setQuantity(Integer stock) {
        quantityProperty().set(stock);
    }
    public Integer getQuantity() {
        return quantityProperty().get();
    }
    public IntegerProperty quantityProperty() {
        if (quantity == null) {
            quantity = new SimpleIntegerProperty();
        }
        return quantity;
    }

    public void setTotalPrice(Double price) {
        priceProperty().set(price);
    }
    public Double getTotalPrice() {
        return priceProperty().get();
    }
    public DoubleProperty priceProperty() {
        if (price == null) {
            price = new SimpleDoubleProperty();
        }
        return price;
    }
}
