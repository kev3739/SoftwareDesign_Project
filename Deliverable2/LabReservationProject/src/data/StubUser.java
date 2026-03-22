package data;

import model.User;

/**
 * Lightweight User stub used when loading reservations from CSV.
 * Only the ID is known at that point.
 */
public class StubUser extends User {

    public StubUser(String userId) {
        super(userId, "", "", userId);
    }

    @Override
    public String getUserType()    { return "Unknown"; }

    @Override
    public double getHourlyRate()  { return 0.0; }
}