package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import mapper.SaleMapper;
import mapper.UserMapper;
import model.validator.Notification;
import service.sale.SaleService;
import service.book.BookService;
import service.report.ReportService;
import service.user.AuthenticationService;
import view.AdminView;
import view.BookView;
import view.model.BookDTO;
import view.model.SaleDTO;
import view.model.UserDTO;
import view.model.builder.BookDTOBuilder;
import view.model.builder.SaleDTOBuilder;
import view.model.builder.UserDTOBuilder;

import java.util.List;

public class AdminController {
    private final AdminView adminView;
    private final BookService bookService;
    private final SaleService saleService;
    private final AuthenticationService authenticationService;
    private final ReportService reportService;
    private final String seller;

    public AdminController(AdminView adminView, BookService bookService, SaleService saleService, AuthenticationService authenticationService, String user) {
        this.adminView = adminView;
        this.bookService = bookService;
        this.saleService = saleService;
        this.authenticationService = authenticationService;
        this.reportService = new ReportService();
        this.seller = user;

        this.adminView.addSaveButtonListener(new SaveButtonListener());
        this.adminView.addSelectionTableListener(new SelectionTableListener());
        this.adminView.addDeleteButtonListener(new DeleteButtonListener());
        this.adminView.addSaleButtonListener(new SaleButtonListener());
        this.adminView.addDeleteSaleButtonListener(new DeleteSaleButtonListener());
        this.adminView.addUserButtonListener(new RegisterButtonListener());
        this.adminView.addDeleteUsersButtonListener(new DeleteUserButtonListener());
        this.adminView.addGenerateButtonListener(new AdminController.GenerateReportButtonListener());
    }
    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String title = adminView.getTitle();
            String author = adminView.getAuthor();
            Integer stock = adminView.getStock();
            Double price = adminView.getPrice();

            if (title.isEmpty() || author.isEmpty()) {
                adminView.displayAlertMessage("Save Error", "Problem at Title or Author fields",
                        "Can not have empty Author or Title fields. Please fill in the fields before submitting Save!");
                adminView.getBooksObservableList().get(0).setTitle("No Name");
            } else {
                BookDTO bookDTO = new BookDTOBuilder()
                        .setAuthor(author)
                        .setTitle(title)
                        .setStock(stock)
                        .setPrice(price)
                        .build();

                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if (savedBook) {
                    adminView.displayAlertMessage("Save Successful", "Book Added", "Book was successfully added to the database.");
                    adminView.addBookToObservableList(bookDTO);
                } else {
                    adminView.displayAlertMessage("Save Not Successful", "Book was not added", "There was a problem adding the book into the database.");
                }
            }
        }
    }

    private class SelectionTableListener implements ChangeListener<BookDTO> {

        @Override
        public void changed(ObservableValue<? extends BookDTO> observable, BookDTO oldValue, BookDTO newValue) {
            if (newValue != null) {
                System.out.println("Book Author: " + newValue.getAuthor() + " Title: " + newValue.getTitle());
            } else {
                System.out.println("No book selected.");
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) adminView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null) {
                boolean deletionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));
                if (deletionSuccessful) {
                    adminView.removeBookFromObservableList(bookDTO);
                } else {
                    adminView.displayAlertMessage("Deletion Not Successful", "Deletion Process",
                            "There was a problem in the deletion process. Please restart the application and try again!");
                }
            } else {
                adminView.displayAlertMessage("Deletion Not Successful", "Deletion Process",
                        "You need to select a row from the table before pressing the delete button!");
            }
        }
    }

    // Listener to handle adding a sale
    private class SaleButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            BookDTO selectedSale = (BookDTO) adminView.getBookTableView().getSelectionModel().getSelectedItem();
            int quantity = adminView.getSaleQuantity();
            String seller_id=seller;
            // Validate input
            if (selectedSale.getTitle().isEmpty() || quantity <= 0) {
                adminView.displayAlertMessage("Sale Error", "Invalid Input",
                        "Please select a book and ensure the quantity is greater than zero.");
                return;
            }

            // Find the selected book
            BookDTO selectedBook = adminView.getBooksObservableList()
                    .stream()
                    .filter(book -> book.getTitle().equals(selectedSale.getTitle()))
                    .findFirst()
                    .orElse(null);

            if (selectedBook == null) {
                adminView.displayAlertMessage("Sale Error", "Book Not Found",
                        "The selected book could not be found.");
                return;
            }

            // Check stock availability
            if (selectedBook.getStock() < quantity) {
                adminView.displayAlertMessage("Sale Error", "Insufficient Stock",
                        "Not enough stock available for this sale.");
                return;
            }

            // Create SaleDTO
            SaleDTO saleDTO = new SaleDTOBuilder()
                    .setBookTitle(selectedBook.getTitle())
                    .setStock(quantity)
                    .setTotalPrice(selectedBook.getPrice() * quantity)
                    .build();

            // Save the sale in the database
            boolean savedSale = saleService.save(SaleMapper.convertSaleDTOToSale(saleDTO),seller);

            if (savedSale) {
                // Update stock for the book
                selectedBook.setStock(selectedBook.getStock() - quantity);
                adminView.getBooksObservableList().set(
                        adminView.getBooksObservableList().indexOf(selectedBook), selectedBook
                );

                    // Add the sale to the UI list
                adminView.addSaleToObservableList(saleDTO);
                if(selectedBook.getStock()==0){
                    boolean deleted=bookService.delete(BookMapper.convertBookDTOToBook(selectedBook));
                    adminView.removeBookFromObservableList(selectedBook);
                }
                // Refresh TableViews
                adminView.getBookTableView().refresh();
                adminView.getSaleTableView().refresh();

                adminView.displayAlertMessage("Sale Successful", "Sale Added",
                        "The sale was successfully added, and stock was updated.");
            } else {
                adminView.displayAlertMessage("Sale Error", "Database Error",
                        "There was an error saving the sale. Please try again.");
            }
        }
    }


    // Listener to handle deleting a sale
    private class DeleteSaleButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            SaleDTO selectedSale = (SaleDTO) adminView.getSaleTableView().getSelectionModel().getSelectedItem();

            if (selectedSale == null) {
                adminView.displayAlertMessage("Deletion Error", "No Selection",
                        "Please select a sale to delete.");
                return;
            }

            boolean deletionSuccessful = saleService.delete(SaleMapper.convertSaleDTOToSale(selectedSale));

            if (deletionSuccessful) {
                // Remove sale from UI list
                adminView.removeSaleFromObservableList(selectedSale);

                // Refresh TableView
                adminView.getSaleTableView().refresh();

                adminView.displayAlertMessage("Deletion Successful", "Sale Deleted",
                        "The sale was successfully deleted.");
            } else {
                adminView.displayAlertMessage("Deletion Error", "Database Error",
                        "There was an error deleting the sale. Please try again.");
            }
        }
    }
    private class RegisterButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.registerFromAdmin(username, password);

            if (registerNotification.hasErrors()) {
                adminView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                adminView.setActionTargetText("Register successful!");

                // Create UserDTO and add to ObservableList
                UserDTO newUserDTO = new UserDTOBuilder()
                        .setUsername(username)
                        .build();

                adminView.getUsersObservableList().add(newUserDTO);
                adminView.getUserTableView().refresh();
            }
        }
    }


    private class DeleteUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            UserDTO selectedUser = (UserDTO) adminView.getUserTableView().getSelectionModel().getSelectedItem();

            if (selectedUser == null) {
                adminView.displayAlertMessage("Deletion Error", "No Selection",
                        "Please select a user to delete.");
                return;
            }

            boolean deletionSuccessful = authenticationService.delete(UserMapper.convertUserDTOToUser(selectedUser));

            if (deletionSuccessful) {
                adminView.removeUserFromObservableList(selectedUser);

                adminView.getUserTableView().refresh();

                adminView.displayAlertMessage("Deletion Successful", "User Deleted",
                        "The user was successfully deleted.");
            } else {
                adminView.displayAlertMessage("Deletion Error", "Database Error",
                        "There was an error deleting the user. Please try again.");
            }
        }
    }
    private class GenerateReportButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                List<SaleDTO> sales = adminView.getSaleObservableList();

                String filePath = "Report.pdf";
                reportService.generateSalesReport(filePath, sales,authenticationService);

                adminView.displayAlertMessage("PDF Generated", "The PDF was generated", "PDF was generated with the name " + filePath);

            } catch (Exception e) {
                e.printStackTrace();
                adminView.displayAlertMessage("PDF Not Generated", "The PDF was not generated", "PDF was not generated.");
            }
        }
    }

}
