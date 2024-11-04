import java.util.Scanner;

public class ConsoleATM {
    private ATM atm;
    private Scanner scanner;

    public ConsoleATM(ATM atm) {
        this.atm = atm;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (true) {
            System.out.println("Welcome to the ATM. Please select an option:");
            System.out.println("1. Insert Card");
            System.out.println("2. Enter PIN");
            System.out.println("3. Check Balance");
            System.out.println("4. Deposit Money");
            System.out.println("5. Withdraw Money");
            System.out.println("6. Exit");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                scanner.nextLine(); // Clear the invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    insertCard();
                    break;
                case 2:
                    enterPin();
                    break;
                case 3:
                    checkBalance();
                    break;
                case 4:
                    depositMoney();
                    break;
                case 5:
                    withdrawMoney();
                    break;
                case 6:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private void insertCard() {
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        if (atm.insertCard(userId)) {
            System.out.println("Card inserted successfully.");
        } else {
            System.out.println("Invalid or locked card.");
        }
    }

    private void enterPin() {
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        if (atm.enterPin(pin)) {
            System.out.println("PIN accepted. You are now logged in.");
        } else {
            System.out.println("Incorrect PIN. Please try again.");
        }
    }

    private void checkBalance() {
        try {
            double balance = atm.checkBalance();
            System.out.println("Current balance: " + balance);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void depositMoney() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        try {
            atm.deposit(amount);
            System.out.println("Deposited: " + amount);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    private void withdrawMoney() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        if (atm.withdraw(amount)) {
            System.out.println("Withdrew: " + amount);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public static void main(String[] args) {
        Bank bank = new Bank(); // Create a bank instance with users
        ATM atm = new ATM(bank); // Create an ATM instance
        ConsoleATM consoleATM = new ConsoleATM(atm); // Create the console application
        consoleATM.start(); // Start the console application
    }
}
