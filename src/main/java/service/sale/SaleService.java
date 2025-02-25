package service.sale;

import model.Sale;

import java.util.*;

public interface SaleService {
    List<Sale> findAll();

    Sale findByTitle(String title);

    boolean save(Sale sale,String seller);

    boolean delete(Sale sale);
}
