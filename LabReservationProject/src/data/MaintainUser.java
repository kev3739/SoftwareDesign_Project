package data;

import factory.UserFactory;
import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading and saving User objects to/from CSV.
 * CSV columns: userType, name, email, password, idNumber, approved
 */
public class MaintainUser {

    public List<User> users = new ArrayList<>();
    private final UserFactory userFactory = new UserFactory();

    public void load(String path) throws Exception {
        users.clear();
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                String  userType  = parts[0].trim();
                String  name      = parts[1].trim();
                String  email     = parts[2].trim();
                String  password  = parts[3].trim();
                String  idNumber  = parts[4].trim();
                boolean approved  = Boolean.parseBoolean(parts[5].trim());

                User user = userFactory.createUser(
                    userType, name, email, password, idNumber, approved
                );
                users.add(user);
            }
        }
    }

    public void update(String path) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            pw.println("userType,name,email,password,idNumber,approved");
            for (User u : users) {
                pw.printf("%s,%s,%s,%s,%s,%b%n",
                    u.getUserType(),
                    u.getName(),
                    u.getEmail(),
                    u.getPassword(),
                    u.getIdNumber(),
                    true
                );
            }
        }
    }

    /** Find a user by email — useful for login */
    public User findByEmail(String email) {
        return users.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElse(null);
    }

    /** Find a user by ID number */
    public User findById(String idNumber) {
        return users.stream()
            .filter(u -> u.getIdNumber().equals(idNumber))
            .findFirst()
            .orElse(null);
    }
}
