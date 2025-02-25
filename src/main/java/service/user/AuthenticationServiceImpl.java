package service.user;
import model.Role;
import model.User;
import model.Builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

import static database.Constants.Roles.*;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> register(String username, String password) {
        Notification<Boolean> notification = new Notification<>();
        if(userRepository.existsByUsername(username)) {
            notification.addError(username + " already exists");
            notification.setResult(Boolean.FALSE);
            return notification;
        }

        Role customerRole = rightsRolesRepository.findRoleByTitle(CUSTOMER);

        if(userRepository.existsByUsername(username)) {
            Notification<Boolean> userRegisterNotification=new Notification<>();
            userRegisterNotification.addError(username + " already exists");
            userRegisterNotification.setResult(false);
            return userRegisterNotification;
        }
        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(customerRole))
                .build();

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate();


        if (!userValid){
            userValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        } else {
            user.setPassword(hashPassword(password));
            notification.setResult(userRepository.save(user));
        }

        return notification;
    }

    @Override
    public Notification<Boolean> registerFromAdmin(String username, String password) {
        Notification<Boolean> notification = new Notification<>();
        if(userRepository.existsByUsername(username)) {
            notification.addError(username + " already exists");
            notification.setResult(Boolean.FALSE);
            return notification;
        }

        Role customerRole = rightsRolesRepository.findRoleByTitle(EMPLOYEE);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(customerRole))
                .build();

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate();


        if (!userValid){
            userValidator.getErrors().forEach(notification::addError);
            notification.setResult(Boolean.FALSE);
        } else {
            user.setPassword(hashPassword(password));
            notification.setResult(userRepository.save(user));
        }

        return notification;
    }

    @Override
    public Notification<User> login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, hashPassword(password));
    }

    @Override
    public Notification<Boolean> isAdmin(String username, String password) {
        Notification<Boolean> notification = new Notification<>();

        if (!userRepository.existsByUsername(username)) {
            notification.addError("User does not exist.");
            notification.setResult(Boolean.FALSE);
            return notification;
        }

        Notification<User> userNotification = userRepository.findByUsernameAndPassword(username, hashPassword(password));
        if (userNotification.hasErrors()) {
            notification.addError("Invalid credentials.");
            notification.setResult(Boolean.FALSE);
            return notification;
        }

        User user = userNotification.getResult();
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> ADMINISTRATOR.equals(role.getRole()));

        notification.setResult(isAdmin);
        return notification;
    }

    @Override
    public boolean logout(User user) {
        return false;
    }

    @Override
    public List<User> finaAll() {
        return userRepository.findAll();
    }

    private String hashPassword(String password) {
        try {
            // Sercured Hash Algorithm - 256
            // 1 byte = 8 biți
            // 1 byte = 1 char
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public String FindByID(String id) {
        return userRepository.FindByID(id);

    }
    @Override
    public boolean delete(User user) {
        return userRepository.deleteUser(user);
    }
}
