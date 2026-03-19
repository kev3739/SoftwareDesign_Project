package model;

public class Faculty extends User {

    private boolean approved;

    public Faculty(String name, String email, String password, String idNumber, boolean approved) {
        super(name, email, password, idNumber);
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }

    @Override
    public String getUserType() {
        return "Faculty";
    }

    @Override
    public double getHourlyRate() {
        return 15.0;
    }
}