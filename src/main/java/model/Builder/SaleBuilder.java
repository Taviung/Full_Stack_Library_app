package model.Builder;

import model.Sale;
import model.User;
import view.model.SaleDTO;

import java.time.LocalDate;
import java.util.Date;

public class SaleBuilder {
    private Sale sale;

    public SaleBuilder() {
        sale = new Sale();
    }

    public SaleBuilder setId(Long id) {
        sale.setId(id);
        return this;
    }

    public SaleBuilder setBookTitle(String title) {
        sale.setBookTitle(title);
        return this;
    }

    public SaleBuilder setQuantity(int stock) {
        sale.setQuantity(stock);
        return this;
    }

    public SaleBuilder setTotalPrice(double price) {
        sale.setTotalPrice(price);
        return this;
    }

    public SaleBuilder setDate() {
        sale.setSelldate(LocalDate.now());
        return this;
    }

    public SaleBuilder setSoldBy(String user) {
        sale.setSeller(user);
        return this;
    }

    public Sale build() {
        return sale;
    }
    
}
