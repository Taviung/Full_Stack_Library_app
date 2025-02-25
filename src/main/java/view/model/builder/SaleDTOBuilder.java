package view.model.builder;

import view.model.SaleDTO;

import java.time.LocalDate;
import java.util.Date;

public class SaleDTOBuilder {
    private SaleDTO saleDTO;

    public SaleDTOBuilder() {
        saleDTO = new SaleDTO();
    }

    public SaleDTOBuilder setBookTitle(String title) {
        saleDTO.setBookTitle(title);
        return this;
    }

    public SaleDTOBuilder setStock(Integer stock) {
        saleDTO.setQuantity(stock);
        return this;
    }

    public SaleDTOBuilder setTotalPrice(Double price) {
        saleDTO.setTotalPrice(price);
        return this;
    }

    public SaleDTOBuilder setSellDate(LocalDate sellDate) {
        saleDTO.setSellDate(sellDate);
        return this;
    }

    public SaleDTOBuilder setSeller(String seller) {
        saleDTO.setSeller(seller);
        return this;
    }

    public SaleDTO build() {
        return saleDTO;
    }
}
