package service.sale;

import model.Sale;
import model.Book;
import repository.sale.SaleRepository;
import repository.book.BookRepository;
import repository.user.UserRepository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final BookRepository bookRepository;

    public SaleServiceImpl(SaleRepository saleRepository, BookRepository bookRepository) {
        this.saleRepository = saleRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean save(Sale sale,String seller) {
        // Fetch the book corresponding to the sale
        Optional<Book> bookOptional = bookRepository.findByTitle(sale.getBookTitle());
        if (bookOptional.isEmpty()) {
            throw new IllegalArgumentException("Book with title: %s not found".formatted(sale.getBookTitle()));
        }

        Book book = bookOptional.get();

        // Check if there is enough stock to process the sale
        if (book.getStock() < sale.getQuantity()) {
            throw new IllegalArgumentException("Not enough stock for book: %s. Available stock: %d".formatted(book.getTitle(), book.getStock()));
        }

        // Calculate the total price of the sale (if necessary, this could include discounts, taxes, etc.)
        double totalPrice = sale.getQuantity() * book.getPrice();
        sale.setTotalPrice(totalPrice);  // Assuming Sale has a `setTotalPrice` method
        // Save the sale in the sale repository

        sale.setSeller(seller);
        boolean saleSaved = saleRepository.saveSale(sale);
        if (!saleSaved) {
            return false;
        }

        // Update the book's stock in the book repository;
        boolean bookUpdated = bookRepository.update(book.getTitle(),book.getStock() - sale.getQuantity()); // Assuming there's an `update` method

        return bookUpdated;
    }

    @Override
    public boolean delete(Sale sale) {
        return saleRepository.deleteSale(sale);
    }

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAllSale();
    }

    @Override
    public Sale findByTitle(String title) {
        Optional<Sale> sale = saleRepository.findSaleByTitle(title);
        return sale.orElseThrow(() -> new IllegalArgumentException("Sale with title: %s was not found".formatted(title)));
    }
}
