import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TableTest {

    Table table = new Table(2);

    TableTest() throws Exception {
    }

    @Test
    void isPriorityTileInCenterTest() {
        assertEquals(true, table.isPriorityTileInCenter() );
    }

    @Test
    void isBagEmpty() {
        assertEquals(false, table.isBagEmpty());
    }

    @Test
    void refillBagTest() throws Exception {
        Exception exception = Assertions.assertThrows(Exception.class, () -> table.refillBag());
        String expectedMessage = "Can't refill bag until it is not empty";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage,
                "The exception message should match the expected message");
    }
}