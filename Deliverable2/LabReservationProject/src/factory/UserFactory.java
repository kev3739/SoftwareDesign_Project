package factory;

import model.Faculty;
import model.Guest;
import model.LabManager;
import model.Researcher;
import model.Student;
import model.User;

public class UserFactory {

    public User createUser(String userType, String name, String email, String password, String idNumber, boolean approved) {
        switch (userType.toLowerCase()) {
            case "student":
                return new Student(name, email, password, idNumber);
            case "faculty":
                return new Faculty(name, email, password, idNumber, approved);
            case "researcher":
                return new Researcher(name, email, password, idNumber, approved);
            case "guest":
                return new Guest(name, email, password, idNumber);
            default:
                throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }

    public LabManager createLabManager(String name, String email, String password, String managerId) {
        return new LabManager(name, email, password, managerId);
    }
}