package service;

import factory.UserFactory;
import model.User;

public class RegistrationService {

    private UserFactory userFactory;

    public RegistrationService() {
        this.userFactory = new UserFactory();
    }

    public User registerUser(String userType, String name, String email, String password, String idNumber, boolean approved) {
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Password must contain uppercase, lowercase, digit, and symbol.");
        }

        return userFactory.createUser(userType, name, email, password, idNumber, approved);
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d]).+$");
    }
}