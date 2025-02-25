package repository.user;

import model.Sale;
import model.User;
import model.validator.Notification;

import java.util.*;

public interface UserRepository {

    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);

    boolean save(User user);

    void removeAll();

    boolean deleteUser(User user);

    boolean existsByUsername(String username);

    String FindByID(String id);
}