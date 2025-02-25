package service.user;

import model.Sale;
import model.User;
import model.validator.Notification;

import java.util.List;

public interface AuthenticationService {
    Notification<Boolean> register(String username, String password);

    Notification<Boolean> registerFromAdmin(String username, String password);

    Notification<User> login(String username, String password);

    Notification<Boolean> isAdmin(String username,String password);

    boolean logout(User user);

    List<User> finaAll();

    boolean delete(User user);

    String FindByID(String id);
}