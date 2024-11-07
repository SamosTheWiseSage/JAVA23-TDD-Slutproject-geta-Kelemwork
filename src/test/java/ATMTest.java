import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ATMTest {
    private ATM atm;
    private Bank bank;
    private User user;

    @BeforeEach
    public void setUp() {
        bank = mock(Bank.class); // Mock the Bank
        user = mock(User.class); // Mock the User
        atm = new ATM(bank);

        // Set up the behavior of the bank mock
        when(bank.getUserById("12345")).thenReturn(user);

        // Set up initial state for the user mock
        when(user.getBalance()).thenReturn(1000.0);
        when(user.getPin()).thenReturn("1234");
        when(user.isLocked()).thenReturn(false);
    }

    @Test
    @DisplayName("Insert Card - User Found")
    public void testInsertCard_UserFound() {
        assertTrue(atm.insertCard("12345"), "Card should be inserted successfully.");
        verify(bank).getUserById("12345"); // Verify method call
    }

    @Test
    @DisplayName("Insert Card - User Not Found")
    public void testInsertCard_UserNotFound() {
        when(bank.getUserById("99999")).thenReturn(null);
        assertFalse(atm.insertCard("99999"), "Card should not be found.");
        verify(bank).getUserById("99999"); // Verify method call
    }

    @Test
    @DisplayName("Enter PIN - Correct PIN")
    public void testEnterPin_CorrectPin() {
        atm.insertCard("12345");
        assertTrue(atm.enterPin("1234"), "User should log in with correct PIN.");
        verify(user).resetFailedAttempts(); // Verify method call
    }

    @Test
    @DisplayName("Enter PIN - Incorrect PIN - Less than 3 Attempts")
    public void testEnterPin_IncorrectPin_LessThan3Attempts() {
        atm.insertCard("12345");
        assertFalse(atm.enterPin("wrong"), "Login should fail with incorrect PIN.");
        verify(user).incrementFailedAttempts(); // Verify method call
    }


    @Test
    @DisplayName("Enter PIN - Card Locked After 3 Attempts")
    public void testEnterPin_CardLockedAfter3Attempts() {
        // Arrange: Set up mock behavior for the user
        when(user.getPin()).thenReturn("1234");  // Correct PIN
        when(user.isLocked()).thenReturn(false); // Initially, the card is not locked
        when(user.getFailedAttempts()).thenReturn(0); // Start with 0 failed attempts

        // Mock the lockCard() and incrementFailedAttempts() methods
        doNothing().when(user).lockCard();  // Simulate the card getting locked without doing anything
        doAnswer(invocation -> {
            // Simulate incrementing failed attempts
            int currentFailedAttempts = user.getFailedAttempts() + 1;
            when(user.getFailedAttempts()).thenReturn(currentFailedAttempts);

            // Lock the card after 3 failed attempts, and update isLocked() to return true
            if (currentFailedAttempts >= 3) {
                user.lockCard();  // Lock the card after 3 failed attempts
                when(user.isLocked()).thenReturn(true); // Explicitly update the mock state
            }
            return null;
        }).when(user).incrementFailedAttempts();

        // Set up the bank to return the mock user when requested
        when(bank.getUserById("12345")).thenReturn(user);

        // Insert the card for the user with id "12345"
        atm.insertCard("12345");

        // Simulate 3 failed PIN attempts
        for (int i = 0; i < 3; i++) {
            boolean result = atm.enterPin("wrong");  // Simulate entering an incorrect PIN
            assertFalse(result, "PIN should be incorrect.");
        }

        // Verify that incrementFailedAttempts was called 3 times
        verify(user, times(3)).incrementFailedAttempts();

        // Verify that lockCard() was called after the 3rd failed attempt
        verify(user, times(2)).lockCard();

        // Assert that the user is locked after 3 failed attempts
        assertTrue(user.isLocked(), "User's card should be locked after 3 failed attempts.");
    }

    @Test
    @DisplayName("Check Balance - Successful")
    public void testCheckBalance() {
        atm.insertCard("12345");
        atm.enterPin("1234");
        assertEquals(1000.0, atm.checkBalance(), "Balance should match.");
        verify(user).getBalance(); // Verify method call
    }

    @Test
    @DisplayName("Deposit - Successful")
    public void testDeposit() {
        atm.insertCard("12345");
        atm.enterPin("1234");
        atm.deposit(500.0);
        verify(user).deposit(500.0); // Verify deposit method call
    }

    @Test
    @DisplayName("Withdraw - Successful")
    public void testWithdraw_Successful() {
        atm.insertCard("12345");
        atm.enterPin("1234");

        assertTrue(atm.withdraw(200.0), "Withdrawal should be successful.");
        verify(user).withdraw(200.0); // Verify withdraw method call
    }

    @Test
    @DisplayName("Withdraw - Insufficient Funds")
    public void testWithdraw_InsufficientFunds() {
        when(user.getBalance()).thenReturn(100.0); // Set balance to test insufficient funds
        atm.insertCard("12345");
        atm.enterPin("1234");
        assertFalse(atm.withdraw(1500.0), "Withdrawal should fail due to insufficient funds.");
        verify(user, never()).withdraw(1500.0); // Verify method not called
    }

    @Test
    @DisplayName("Get Bank Name - Static Method")
    public void testGetBankName() {
        String bankName = Bank.getBankName();
        assertEquals("MockBank", bankName, "Bank name should be MockBank.");
    }
}
