package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import mapper.SaleMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.sale.SaleRepository;
import repository.sale.SaleRepositoryMySQL;
import service.sale.SaleService;
import service.sale.SaleServiceImpl;
import service.book.BookService;
import service.book.BookServiceImpl;
import view.BookView;
import view.model.BookDTO;
import view.model.SaleDTO;

import java.sql.Connection;
import java.util.List;

public class EmployeeComponentFactory {

    private final BookView bookView;
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final SaleRepository SaleRepository;
    private final BookService bookService;
    private final SaleService SaleService;
    private final String user;
    private volatile static EmployeeComponentFactory instance;

    public static synchronized EmployeeComponentFactory getInstance(Boolean componentsForTest, Stage stage,String user){
        if (instance == null){
            instance = new EmployeeComponentFactory(componentsForTest, stage, user);
        }
        return instance;
    }

    private EmployeeComponentFactory(Boolean componentsForTest, Stage stage,String user){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.SaleRepository = new SaleRepositoryMySQL(connection);
        this.user = user;

        this.bookService = new BookServiceImpl(bookRepository);
        this.SaleService = new SaleServiceImpl(SaleRepository,bookRepository);

        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(this.bookService.findAll());
        List<SaleDTO> saleDTOS = SaleMapper.convertSaleListToSaleDTOList(this.SaleRepository.findAllSale());

        this.bookView = new BookView(stage, bookDTOs, saleDTOS);
        this.bookController = new BookController(bookView, bookService, SaleService,user);
    }

    public BookView getBookView() {
        return bookView;
    }

    public BookController getBookController() {
        return bookController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }

    public static EmployeeComponentFactory getInstance() {
        return instance;
    }
}
