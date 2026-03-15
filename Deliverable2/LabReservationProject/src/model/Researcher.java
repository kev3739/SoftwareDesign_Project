package model;

public class Researcher extends User {

    private boolean approved;

    public Researcher(String name, String email, String password, String idNumber, boolean approved) {
        super(name, email, password, idNumber);
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }

    @Override
    public String getUserType() {
        return "Researcher";
    }

    @Override
    public double getHourlyRate() {
        return 20.0;
    }
}