package model;

public class Student extends User {

    public Student(String name, String email, String password, String idNumber) {
        super(name, email, password, idNumber);
    }

    @Override
    public String getUserType() {
        return "Student";
    }

    @Override
    public double getHourlyRate() {
        return 10.0;
    }
}