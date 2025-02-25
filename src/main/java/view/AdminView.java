package view;

import view.model.BookDTO;
import view.model.SaleDTO;
import view.model.UserDTO;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class AdminView {
    private TableView bookTableView;
    private TableView saleTableView;
    private TableView userTableView; // TableView for users

    private ObservableList<BookDTO> booksObservableList;
    private ObservableList<SaleDTO> saleObservableList;
    private ObservableList<UserDTO> usersObservableList; // ObservableList for users

    private TextField authorTextField;
    private TextField titleTextField;
    private TextField amounTextField;
    private TextField priceTextField;
    private TextField saleQuantityTextField;

    private TextField usernameTextField; // Field for username
    private TextField passwordTextField; // Field for password
    private Button addUserButton; // Button for adding users
    private Button deleteUsersButton; // Button for deleting all users

    private Text actiontarget; // New feedback field

    private Label authorLabel;
    private Label titleLabel;
    private Label stockLabel;
    private Label priceLabel;
    private Label saleQuantityLabel;
    private Label usernameLabel; // Label for username
    private Label passwordLabel; // Label for password

    private Button saveButton;
    private Button deleteButton;
    private Button saleButton;
    private Button deleteSaleButton;
    private Button generateButton;

    public AdminView(Stage primaryStage, List<BookDTO> bookDTOS, List<SaleDTO> saleDTOS, List<UserDTO> userDTOS) {
        primaryStage.setTitle("Library");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 1400, 700); // Increased width for additional fields
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(bookDTOS);
        saleObservableList = FXCollections.observableArrayList(saleDTOS);
        usersObservableList = FXCollections.observableArrayList(userDTOS); // Initialize users list

        initTableViewBook(gridPane);
        initTableSale(gridPane);
        initTableViewUser(gridPane); // Initialize the users TableView

        initSaveOptions(gridPane);
        initUserCreationOptions(gridPane); // Initialize user creation fields

        initActionTarget(gridPane); // Initialize actiontarget

        primaryStage.show();
    }

    private void initTableViewBook(GridPane gridPane) {
        bookTableView = new TableView<>();
        bookTableView.setPlaceholder(new Label("No rows to display"));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookDTO, Number> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn<BookDTO, Number> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        bookTableView.getColumns().addAll(titleColumn, authorColumn, stockColumn, priceColumn);

        bookTableView.setItems(booksObservableList);

        gridPane.add(bookTableView, 0, 0, 5, 1);
    }

    private void initTableSale(GridPane gridPane) {
        saleTableView = new TableView<>();
        saleTableView.setPlaceholder(new Label("No rows to display"));

        TableColumn<SaleDTO, String> bookTitleColumn = new TableColumn<>("Title Book");
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        TableColumn<SaleDTO, Number> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<SaleDTO, Number> totalPriceColumn = new TableColumn<>("Total price");
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        saleTableView.getColumns().addAll(bookTitleColumn, quantityColumn, totalPriceColumn);

        saleTableView.setItems(saleObservableList);

        gridPane.add(saleTableView, 5, 0, 5, 1);
    }

    private void initTableViewUser(GridPane gridPane) {
        userTableView = new TableView<>();
        userTableView.setPlaceholder(new Label("No users to display"));

        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        userTableView.getColumns().addAll(usernameColumn);

        userTableView.setItems(usersObservableList);

        gridPane.add(userTableView, 0, 6, 5, 1); // Position the user table
    }

    private void initSaveOptions(GridPane gridPane) {
        titleLabel = new Label("Title");
        gridPane.add(titleLabel, 1, 1);

        titleTextField = new TextField();
        gridPane.add(titleTextField, 2, 1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel, 3, 1);

        authorTextField = new TextField();
        gridPane.add(authorTextField, 4, 1);

        stockLabel = new Label("Stock");
        gridPane.add(stockLabel, 5, 1);

        amounTextField = new TextField();
        gridPane.add(amounTextField, 6, 1);

        priceLabel = new Label("Price");
        gridPane.add(priceLabel, 7, 1);

        priceTextField = new TextField();
        gridPane.add(priceTextField, 8, 1);

        saleQuantityLabel = new Label("Quantity");
        gridPane.add(saleQuantityLabel, 3, 3);

        saleQuantityTextField = new TextField();
        gridPane.add(saleQuantityTextField, 4, 3);

        saveButton = new Button("Save");
        gridPane.add(saveButton, 1, 2);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 2, 2);

        saleButton = new Button("Sale");
        gridPane.add(saleButton, 3, 2);

        deleteSaleButton = new Button("Delete");
        gridPane.add(deleteSaleButton, 4, 2);

        generateButton = new Button("Generate Report");
        gridPane.add(generateButton,7,3);
    }

    private void initUserCreationOptions(GridPane gridPane) {
        usernameLabel = new Label("Username");
        gridPane.add(usernameLabel, 1, 5);

        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 2, 5);

        passwordLabel = new Label("Password");
        gridPane.add(passwordLabel, 3, 5);

        passwordTextField = new TextField();
        gridPane.add(passwordTextField, 4, 5);

        addUserButton = new Button("Add User");
        gridPane.add(addUserButton, 5, 5);

        deleteUsersButton = new Button("Delete User");
        gridPane.add(deleteUsersButton, 6, 5); // Button to delete all users
    }

    private void initActionTarget(GridPane gridPane) {
        actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        gridPane.add(actiontarget, 1, 7); // Adjust position
    }

    private void initializeGridPane(GridPane gridPane) {
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    public void addDeleteUsersButtonListener(EventHandler<ActionEvent> deleteUsersButtonListener) {
        deleteUsersButton.setOnAction(deleteUsersButtonListener);
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener) {
        saveButton.setOnAction(saveButtonListener);
    }

    public void addGenerateButtonListener(EventHandler<ActionEvent> generateButtonListener) {
        generateButton.setOnAction(generateButtonListener);
    }

    public void addSelectionTableListener(ChangeListener selectionTableListener) {
        bookTableView.getSelectionModel().selectedItemProperty().addListener(selectionTableListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener) {
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addSaleButtonListener(EventHandler<ActionEvent> saleButtonListener) {
        saleButton.setOnAction(saleButtonListener);
    }

    public void addDeleteSaleButtonListener(EventHandler<ActionEvent> deleteSaleButtonListener) {
        deleteSaleButton.setOnAction(deleteSaleButtonListener);
    }

    public void addUserButtonListener(EventHandler<ActionEvent> addUserButtonListener) {
        addUserButton.setOnAction(addUserButtonListener);
    }

    public void setActionTargetText(String message) {
        actiontarget.setText(message);
    }

    public String getUsername() {
        return usernameTextField.getText();
    }

    public String getPassword() {
        return passwordTextField.getText();
    }

    public ObservableList<BookDTO> getBooksObservableList() {
        return booksObservableList;
    }

    public ObservableList<SaleDTO> getSaleObservableList() {
        return saleObservableList;
    }
    public int getStock(){
        return Integer.parseInt(amounTextField.getText());
    }

    public double getPrice(){
        return Double.parseDouble(priceTextField.getText());
    }

    public int getSaleQuantity() {
        return Integer.parseInt(saleQuantityTextField.getText());
    }

    public void addBookToObservableList(BookDTO bookDTO){
        this.booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO){
        this.booksObservableList.remove(bookDTO);
    }

    public void addSaleToObservableList(SaleDTO saleDTO){
        this.saleObservableList.add(saleDTO);
    }

    public void removeSaleFromObservableList(SaleDTO saleDTO){
        this.saleObservableList.remove(saleDTO);
    }

    public TableView getBookTableView(){
        return bookTableView;
    }

    public TableView getSaleTableView(){
        return saleTableView;
    }

    public String getTitle(){
        return titleTextField.getText();
    }

    public String getAuthor(){
        return authorTextField.getText();
    }

    public void displayAlertMessage(String titleInformation, String headerInformation, String contextInformation){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleInformation);
        alert.setHeaderText(headerInformation);
        alert.setContentText(contextInformation);

        alert.showAndWait();
    }

    public void removeUserFromObservableList(UserDTO selectedUser) {this.usersObservableList.remove(selectedUser);}

    public TableView getUserTableView() {return userTableView;
    }

    public ObservableList<UserDTO> getUsersObservableList() {
        return usersObservableList;
    }
}

