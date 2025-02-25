package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import launcher.AdminComponentFactory;
import launcher.EmployeeComponentFactory;
import launcher.LoginComponentFactory;
import model.User;
import model.validator.Notification;
import service.user.AuthenticationService;
import view.CustomerView;
import view.LoginView;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;

    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors()) {
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
                return;
            }

            User loggedInUser = loginNotification.getResult();
            String id_logged_in=String.valueOf(loggedInUser.getId());

            if (loggedInUser == null) {
                loginView.setActionTargetText("Login failed! Invalid credentials.");
                return;
            }

            if (authenticationService.isAdmin(username, password).getResult()) {
                loginView.setActionTargetText("Login Successful! Redirecting to Admin Panel...");
                AdminComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage(),id_logged_in);
            } else if (loggedInUser.getRoles().stream().anyMatch(role -> role.getRole().equals("employee"))) {
                loginView.setActionTargetText("Login Successful! Redirecting to Employee Panel...");
                EmployeeComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage(),id_logged_in);
            } else {
                loginView.setActionTargetText("Login Successful! Redirecting to Customer Screen...");
                Stage primaryStage = LoginComponentFactory.getStage();
                new CustomerView(primaryStage);
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            } else {
                loginView.setActionTargetText("Register successful!");
            }
        }
    }
}
