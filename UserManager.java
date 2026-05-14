import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;

public class UserManager {
    private ArrayList<User> users = new ArrayList<>();
    private static final String FILE_NAME = "users.txt";

    // Constructor — loads users when program starts
    public UserManager() {
        loadFromFile();
    }

    // REGISTER
    public boolean register(String username, String password) {
        // check if username already exists
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                System.out.println("❌ Username already exists. Try another.");
                return false;
            }
        }
        // check empty username or password
        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("❌ Username and password cannot be empty.");
            return false;
        }
        // check password length
        if (password.length() < 4) {
            System.out.println("❌ Password must be at least 4 characters.");
            return false;
        }
        User newUser = new User(username, password);
        users.add(newUser);
        saveToFile();
        System.out.println("✅ Registered successfully! You can now login.");
        return true;
    }

    // LOGIN — returns username if success, null if fail
    public String login(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username) &&
                u.getPassword().equals(password)) {
                return u.getUsername();   // login success
            }
        }
        return null;   // login failed
    }

    // SAVE to users.txt
    private void saveToFile() {
        try {
            FileWriter writer = new FileWriter(FILE_NAME);
            for (User u : users) {
                writer.write(u.toFileString() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("❌ Error saving users: " + e.getMessage());
        }
    }

    // LOAD from users.txt
    private void loadFromFile() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) return;
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    users.add(new User(parts[0], parts[1]));
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("❌ Error loading users: " + e.getMessage());
        }
    }
}