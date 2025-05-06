import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private Message msg;

    @BeforeEach
    void setUp() {
        msg = new Message("ATM1", "Server", "JohnDoe1234,12345", Message.Type.LOGINREQATM);
    }

    @Test
    void testConstructorAssignsValuesCorrectly() {
        assertNotNull(msg);  // Ensure object was created
    }

    @Test
    void testGetFrom() {
        assertEquals("ATM1", msg.getFrom());
    }

    @Test
    void testGetTo() {
        assertEquals("Server", msg.getTo());
    }

    @Test
    void testGetData() {
        assertEquals("JohnDoe1234,12345", msg.getData());
    }

    @Test
    void testGetType() {
        assertEquals(Message.Type.LOGINREQATM, msg.getType());
    }

    @Test
    void testGetIdAndIncrements() {
        int firstId = msg.getId();
        Message msg2 = new Message("ATM1", "Server", "JohnDoe1234,12345", Message.Type.LOGINREQATM);
        int secondId = msg2.getId();
        assertEquals(firstId + 1, secondId);
    }

    @Test
    void testToStringContainsFields() {
        String output = msg.toString();
        assertTrue(output.contains("ATM1"));
        assertTrue(output.contains("Server"));
        assertTrue(output.contains("JohnDoe1234,12345"));
        assertTrue(output.contains("LOGINREQATM"));
    }
}
