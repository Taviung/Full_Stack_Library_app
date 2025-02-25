package mapper;

import model.Builder.SaleBuilder;
import model.Sale;
import view.model.builder.SaleDTOBuilder;
import view.model.SaleDTO;

import java.util.List;
import java.util.stream.Collectors;

public class SaleMapper {
    public static SaleDTO convertSaleToSaleDTO(Sale sale) {
        return new SaleDTOBuilder().setBookTitle(sale.getBookTitle()).setStock(sale.getQuantity()).setTotalPrice(sale.getTotalPrice()).setSellDate(sale.getSelldate()).setSeller(sale.getSeller()).build();
    }

    public static Sale convertSaleDTOToSale(SaleDTO saleDTO) {
        return new SaleBuilder().setBookTitle(saleDTO.getBookTitle()).setQuantity(saleDTO.getQuantity()).setTotalPrice(saleDTO.getTotalPrice()).setDate().setSoldBy(saleDTO.getSeller()).build();
    }

    public static List<Sale> convertSaleDTOToSaleList(List<SaleDTO> saleDTOS) {
        return saleDTOS.parallelStream()
                .map(SaleMapper::convertSaleDTOToSale)
                .collect(Collectors.toList());
    }

    public static List<SaleDTO> convertSaleListToSaleDTOList(List<Sale> sales) {
        return sales.parallelStream()
                .map(SaleMapper::convertSaleToSaleDTO)
                .collect(Collectors.toList());
    }
}
