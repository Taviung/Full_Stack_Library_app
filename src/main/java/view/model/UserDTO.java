package view.model;

import javafx.beans.property.*;

public class UserDTO {
    private StringProperty username;

    public void setUsername(String username) {
        usernameProperty().set(username);
    }

    public String getUsername() {
        return usernameProperty().get();
    }

    public StringProperty usernameProperty() {
        if (username == null) {
            username = new SimpleStringProperty();
        }

        return username;
    }
}
