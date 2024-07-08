import com.aproteam.flowerpost.model.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStatusTest {
	/**
	 * Tests that check if the status of the order is correct
	 */
	@Test
	public void testValues() {
		OrderStatus[] expected = {OrderStatus.CREATED, OrderStatus.PAID, OrderStatus.DELIVERY, OrderStatus.COMPLETED};
		assertArrayEquals(expected, OrderStatus.values());
	}

	@Test
	public void testValueOf() {
		assertEquals(OrderStatus.PAID, OrderStatus.valueOf("PAID"));
	}

}