package model;

public abstract class User {
    protected String name;
    protected String email;
    protected String password;
    protected String idNumber;

    public User(String name, String email, String password, String idNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public abstract String getUserType();
    public abstract double getHourlyRate();
}