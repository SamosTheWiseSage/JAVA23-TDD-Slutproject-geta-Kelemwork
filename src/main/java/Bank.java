import java.util.HashMap;
import java.util.Map;

public class Bank implements BankInterface {
    private Map<String, User> users = new HashMap<>();

    public Bank() {
        // Initialize with some example users
        users.put("12345", new User("12345", "1234", 1000.0));
        users.put("67890", new User("67890", "5678", 500.0));
    }

    @Override
    public User getUserById(String id) {
        // Mock behavior: Simulate getting user from a data source
        return users.get(id);
    }

    @Override
    public boolean isCardLocked(String userId) {
        User user = users.get(userId);
        // Mock behavior: Check if the user's card is locked
        return user != null && user.isLocked();
    }

    public static String getBankName() {
        // Mock behavior: Return the name of the bank
        return "MockBank";
    }

    // Mocking method to simulate adding users for testing purposes
    public void addUser(String id, String pin, double balance) {
        users.put(id, new User(id, pin, balance));
    }

    // Additional mock methods for testing
    public void lockUserCard(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.lockCard();
        }
    }

    public void unlockUserCard(String userId) {
        User user = users.get(userId);
        if (user != null) {
            user.lockCard(); // Assuming lockCard method can also unlock if needed
        }
    }
}
