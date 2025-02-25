package launcher;

import controller.AdminController;
import controller.BookController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import mapper.SaleMapper;
import mapper.UserMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.sale.SaleRepository;
import repository.sale.SaleRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.sale.SaleService;
import service.sale.SaleServiceImpl;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.AdminView;
import view.BookView;
import view.model.BookDTO;
import view.model.SaleDTO;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

public class AdminComponentFactory {

    private volatile static AdminComponentFactory instance;
    private final AdminView adminView;
    private final BookRepository bookRepository;
    private final SaleRepository saleRepository;
    private final BookService bookService;
    private final SaleService saleService;
    private final AdminController adminController;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final String user;

    private AdminComponentFactory(Boolean componentsForTest, Stage stage,String user_id) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.saleRepository = new SaleRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService=new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        this.saleService = new SaleServiceImpl(saleRepository, bookRepository);
        this.user=user_id;

        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(this.bookService.findAll());
        List<SaleDTO> saleDTOs = SaleMapper.convertSaleListToSaleDTOList(this.saleService.findAll());
        List<UserDTO> userDTOs = UserMapper.convertUserListToUserDTOList(this.authenticationService.finaAll());

        this.adminView = new AdminView(stage, bookDTOs, saleDTOs, userDTOs);

        this.adminController = new AdminController(adminView, bookService, saleService, authenticationService, user);
    }

    public static synchronized AdminComponentFactory getInstance(Boolean componentsForTest, Stage stage,String user_id) {
        if (instance == null) {
            instance = new AdminComponentFactory(componentsForTest, stage,user_id);
        }
        return instance;
    }

    public AdminView getAdminView() {
        return adminView;
    }

    public AdminController getAdminController() {
        return adminController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }
}
