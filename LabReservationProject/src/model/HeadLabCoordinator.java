package model;

import factory.UserFactory;

public class HeadLabCoordinator {

    private static HeadLabCoordinator instance;
    private UserFactory userFactory;

    private HeadLabCoordinator() {
        this.userFactory = new UserFactory();
    }

    public static HeadLabCoordinator getInstance() {
        if (instance == null) {
            instance = new HeadLabCoordinator();
        }
        return instance;
    }

    public LabManager createLabManagerAccount(String name, String email, String password, String managerId) {
        return userFactory.createLabManager(name, email, password, managerId);
    }
}