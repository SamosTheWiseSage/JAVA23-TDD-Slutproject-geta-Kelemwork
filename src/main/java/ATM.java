public class ATM {
    private Bank bank;
    private User currentUser;

    public ATM(Bank bank) {
        this.bank = bank;
    }

    public boolean insertCard(String userId) {
        currentUser = bank.getUserById(userId);
        if (currentUser != null && !currentUser.isLocked()) {
            return true; // Card inserted successfully
        }
        return false; // Card is invalid or locked
    }

    public boolean enterPin(String pin) {
        if (currentUser == null) {
            throw new IllegalStateException("No card inserted.");
        }
        if (currentUser.getPin().equals(pin)) {
            currentUser.resetFailedAttempts();
            return true; // Login successful
        } else {
            currentUser.incrementFailedAttempts();
            if (currentUser.getFailedAttempts() >= 3) {
                currentUser.lockCard(); // Lock the card after 3 failed attempts
            }
            return false; // Login failed
        }
    }

    public double checkBalance() {
        if (currentUser != null) {
            return currentUser.getBalance();
        }
        throw new IllegalStateException("No user logged in.");
    }

    public void deposit(double amount) {
        if (currentUser != null) {
            currentUser.deposit(amount);
        } else {
            throw new IllegalStateException("No user logged in.");
        }
    }

    public boolean withdraw(double amount) {
        if (currentUser != null) {
            if (currentUser.getBalance() >= amount) {
                currentUser.withdraw(amount);
                return true; // Withdrawal successful
            }
            return false; // Insufficient funds
        }
        throw new IllegalStateException("No user logged in.");
    }
}

