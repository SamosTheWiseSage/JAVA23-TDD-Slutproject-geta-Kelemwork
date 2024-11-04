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
        bank = mock(Bank.class);
        atm = new ATM(bank);
        user = new User("12345", "1234", 1000.0);
        when(bank.getUserById("12345")).thenReturn(user);
    }

    @Test
    @DisplayName("Insert Card - User Found")
    public void testInsertCard_UserFound() {
        assertTrue(atm.insertCard("12345"), "Card should be inserted successfully.");
        verify(bank).getUserById("12345"); // Verify the method was called
    }

    @Test
    @DisplayName("Insert Card - User Not Found")
    public void testInsertCard_UserNotFound() {
        when(bank.getUserById("99999")).thenReturn(null);
        assertFalse(atm.insertCard("99999"), "Card should not be found.");
        verify(bank).getUserById("99999"); // Verify the method was called
    }

    @Test
    @DisplayName("Enter PIN - Correct PIN")
    public void testEnterPin_CorrectPin() {
        atm.insertCard("12345");
        assertTrue(atm.enterPin("1234"), "User should log in with correct PIN.");
        verify(user).resetFailedAttempts(); // Verify the method was called
    }

    @Test
    @DisplayName("Enter PIN - Incorrect PIN - Less than 3 Attempts")
    public void testEnterPin_IncorrectPin_LessThan3Attempts() {
        atm.insertCard("12345");
        assertFalse(atm.enterPin("wrong"), "Login should fail with incorrect PIN.");
        assertEquals(1, user.getFailedAttempts(), "Failed attempts should increment.");
        verify(user).incrementFailedAttempts(); // Verify the method was called
    }

    @Test
    @DisplayName("Enter PIN - Card Locked After 3 Attempts")
    public void testEnterPin_CardLockedAfter3Attempts() {
        atm.insertCard("12345");

        for (int i = 0; i < 3; i++) {
            atm.enterPin("wrong");
        }

        assertTrue(user.isLocked(), "Card should be locked after 3 incorrect attempts.");
        verify(user, times(3)).incrementFailedAttempts(); // Verify the method was called 3 times
        verify(user).lockCard(); // Verify the lockCard method was called
    }

    @Test
    @DisplayName("Check Balance - Successful")
    public void testCheckBalance() {
        atm.insertCard("12345");
        atm.enterPin("1234");
        assertEquals(1000.0, atm.checkBalance(), "Balance should match.");
        verify(user).getBalance(); // Verify the method was called
    }

    @Test
    @DisplayName("Deposit - Successful")
    public void testDeposit() {
        atm.insertCard("12345");
        atm.enterPin("1234");
        atm.deposit(500.0);
        assertEquals(1500.0, user.getBalance(), "Balance should be updated after deposit.");
        verify(user).deposit(500.0); // Verify the deposit method was called
    }

    @Test
    @DisplayName("Withdraw - Successful")
    public void testWithdraw_Successful() {
        atm.insertCard("12345");
        atm.enterPin("1234");

        assertTrue(atm.withdraw(200.0), "Withdrawal should be successful.");
        assertEquals(800.0, user.getBalance(), "Balance should reflect the withdrawal.");
        verify(user).withdraw(200.0); // Verify the withdraw method was called
    }

    @Test
    @DisplayName("Withdraw - Insufficient Funds")
    public void testWithdraw_InsufficientFunds() {
        atm.insertCard("12345");
        atm.enterPin("1234");
        assertFalse(atm.withdraw(1500.0), "Withdrawal should fail due to insufficient funds.");
        verify(user, never()).withdraw(1500.0); // Verify withdraw method was NOT called
    }

    @Test
    @DisplayName("Get Bank Name - Static Method")
    public void testGetBankName() {
        String bankName = Bank.getBankName();
        assertEquals("MockBank", bankName, "Bank name should be MockBank.");
    }
}
