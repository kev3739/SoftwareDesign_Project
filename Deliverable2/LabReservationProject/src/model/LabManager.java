package model;

public class LabManager extends User {

    private String managerId;

    public LabManager(String name, String email, String password, String managerId) {
        super(name, email, password, managerId);
        this.managerId = managerId;
    }

    public String getManagerId() {
        return managerId;
    }

    @Override
    public String getUserType() {
        return "LabManager";
    }

    @Override
    public double getHourlyRate() {
        return 0.0;
    }
}