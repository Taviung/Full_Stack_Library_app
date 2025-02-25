package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerView {

    public CustomerView(Stage primaryStage) {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);

        Label messageLabel = new Label("You are not an employee, please contact an employee for further assistance.");
        vbox.getChildren().add(messageLabel);

        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Customer Assistance");
        primaryStage.show();
    }
}
