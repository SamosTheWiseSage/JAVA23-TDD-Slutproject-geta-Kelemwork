
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("123", "4567", 1000.0);
    }

    @DisplayName("Test initial balance")
    @Test
    void testInitialBalance() {
        assertEquals(1000.0, user.getBalance());
    }

    @DisplayName("Test deposit method")
    @Test
    void testDeposit() {
        user.deposit(500.0);
        assertEquals(1500.0, user.getBalance());
    }

    @DisplayName("Test withdraw method")
    @Test
    void testWithdraw() {
        user.withdraw(200.0);
        assertEquals(800.0, user.getBalance());
    }

    @DisplayName("Test lockCard method")
    @Test
    void testLockCard() {
        user.lockCard();
        assertTrue(user.isLocked());
    }

    @DisplayName("Test incrementFailedAttempts method")
    @Test
    void testIncrementFailedAttempts() {
        user.incrementFailedAttempts();
        assertEquals(1, user.getFailedAttempts());
    }

    @DisplayName("Test resetFailedAttempts method")
    @Test
    void testResetFailedAttempts() {
        user.incrementFailedAttempts();
        user.resetFailedAttempts();
        assertEquals(0, user.getFailedAttempts());
    }
}