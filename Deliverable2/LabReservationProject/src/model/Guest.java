package model;

public class Guest extends User {

    public Guest(String name, String email, String password, String idNumber) {
        super(name, email, password, idNumber);
    }

    @Override
    public String getUserType() {
        return "Guest";
    }

    @Override
    public double getHourlyRate() {
        return 30.0;
    }
}