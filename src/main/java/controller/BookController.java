package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import mapper.SaleMapper;
import service.sale.SaleService;
import service.book.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.SaleDTO;
import view.model.builder.BookDTOBuilder;
import view.model.builder.SaleDTOBuilder;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    private final SaleService saleService;
    private final String user;

    public BookController(BookView bookView, BookService bookService, SaleService saleService,String user){
        this.bookView = bookView;
        this.bookService = bookService;
        this.saleService = saleService;
        this.user = user;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addSelectionTableListener(new SelectionTableListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSaleButtonListener(new SaleButtonListener());
        this.bookView.addDeleteSaleButtonListener(new DeleteSaleButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            Integer stock = bookView.getStock();
            Double price = bookView.getPrice();

            if (title.isEmpty() || author.isEmpty()) {
                bookView.displayAlertMessage("Save Error", "Problem at Title or Author fields",
                        "Can not have empty Author or Title fields. Please fill in the fields before submitting Save!");
                bookView.getBooksObservableList().get(0).setTitle("No Name");
            } else {
                BookDTO bookDTO = new BookDTOBuilder()
                        .setAuthor(author)
                        .setTitle(title)
                        .setStock(stock)
                        .setPrice(price)
                        .build();

                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if (savedBook) {
                    bookView.displayAlertMessage("Save Successful", "Book Added", "Book was successfully added to the database.");
                    bookView.addBookToObservableList(bookDTO);
                } else {
                    bookView.displayAlertMessage("Save Not Successful", "Book was not added", "There was a problem adding the book into the database.");
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
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null) {
                boolean deletionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));
                if (deletionSuccessful) {
                    bookView.removeBookFromObservableList(bookDTO);
                } else {
                    bookView.displayAlertMessage("Deletion Not Successful", "Deletion Process",
                            "There was a problem in the deletion process. Please restart the application and try again!");
                }
            } else {
                bookView.displayAlertMessage("Deletion Not Successful", "Deletion Process",
                        "You need to select a row from the table before pressing the delete button!");
            }
        }
    }

    // Listener to handle adding a sale
    private class SaleButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            BookDTO selectedSale = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            int quantity = bookView.getSaleQuantity();

            // Validate input
            if (selectedSale.getTitle().isEmpty() || quantity <= 0) {
                bookView.displayAlertMessage("Sale Error", "Invalid Input",
                        "Please select a book and ensure the quantity is greater than zero.");
                return;
            }

            // Find the selected book
            BookDTO selectedBook = bookView.getBooksObservableList()
                    .stream()
                    .filter(book -> book.getTitle().equals(selectedSale.getTitle()))
                    .findFirst()
                    .orElse(null);

            if (selectedBook == null) {
                bookView.displayAlertMessage("Sale Error", "Book Not Found",
                        "The selected book could not be found.");
                return;
            }

            // Check stock availability
            if (selectedBook.getStock() < quantity) {
                bookView.displayAlertMessage("Sale Error", "Insufficient Stock",
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
            boolean savedSale = saleService.save(SaleMapper.convertSaleDTOToSale(saleDTO),user);

            if (savedSale) {
                // Update stock for the book
                selectedBook.setStock(selectedBook.getStock() - quantity);
                bookView.getBooksObservableList().set(
                        bookView.getBooksObservableList().indexOf(selectedBook), selectedBook
                );

                // Add the sale to the UI list
                bookView.addSaleToObservableList(saleDTO);

                // Refresh TableViews
                bookView.getBookTableView().refresh();
                bookView.getSaleTableView().refresh();

                bookView.displayAlertMessage("Sale Successful", "Sale Added",
                        "The sale was successfully added, and stock was updated.");
            } else {
                bookView.displayAlertMessage("Sale Error", "Database Error",
                        "There was an error saving the sale. Please try again.");
            }
        }
    }


    // Listener to handle deleting a sale
    private class DeleteSaleButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            SaleDTO selectedSale = (SaleDTO) bookView.getSaleTableView().getSelectionModel().getSelectedItem();

            if (selectedSale == null) {
                bookView.displayAlertMessage("Deletion Error", "No Selection",
                        "Please select a sale to delete.");
                return;
            }

            boolean deletionSuccessful = saleService.delete(SaleMapper.convertSaleDTOToSale(selectedSale));

            if (deletionSuccessful) {
                // Remove sale from UI list
                bookView.removeSaleFromObservableList(selectedSale);

                // Refresh TableView
                bookView.getSaleTableView().refresh();

                bookView.displayAlertMessage("Deletion Successful", "Sale Deleted",
                        "The sale was successfully deleted.");
            } else {
                bookView.displayAlertMessage("Deletion Error", "Database Error",
                        "There was an error deleting the sale. Please try again.");
            }
        }
    }

}
