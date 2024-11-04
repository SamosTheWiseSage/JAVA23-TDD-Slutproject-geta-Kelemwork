import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class BankTest {
    private Bank bank;

    @BeforeEach
    public void setUp() {
        bank = mock(Bank.class); // Create a mock of the Bank class
    }


    @Test
    @DisplayName("Test getUserById method")
    void testGetUserById() {
        User user = new User("12345", "1234", 1000.0);
        when(bank.getUserById("12345")).thenReturn(user);

        User retrievedUser = bank.getUserById("12345");
        assertEquals("12345", retrievedUser.getId());
        assertEquals(1000.0, retrievedUser.getBalance());
        verify(bank).getUserById("12345");
    }

    @Test
    @DisplayName("Test isCardLocked method")
    void testIsCardLocked() {
        User user = new User("67890", "5678", 500.0);
        user.lockCard(); // Lock the user card
        when(bank.isCardLocked("67890")).thenReturn(true);

        assertTrue(bank.isCardLocked("67890"));
        verify(bank).isCardLocked("67890");
    }

    @Test
    @DisplayName("Test static getBankName method")
    void testGetBankName() {
        // Using mockito to verify static method
        assertEquals("MockBank", Bank.getBankName());
    }
}
