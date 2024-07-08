import com.aproteam.flowerpost.Utils;
import com.aproteam.flowerpost.model.Order;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static com.aproteam.flowerpost.model.OrderStatus.PAID;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderTest {

	/**
	 * A test that checks if the ID of the order was added correctly
	 */
	@Test
	public void testGetOrderId() {
		Order order = new Order(1, new Date(), "test@email.com", 1, 2, 20, PAID);
		assertEquals(1, order.getOrderId());
	}

	/**
	 * A test that checks if the date of the order is correct
	 */
	@Test
	public void testGetOrderDate() {
		Date date = new Date();
		Order order = new Order(1, date, "test@email.com", 1, 2, 20, PAID);
		assertEquals(date, order.getOrderDate());
	}

	/**
	 * A test that checks if the e-mail address of the customer that ordered was added correctly
	 */
	@Test
	public void testGetUserEmail() {
		Order order = new Order(1, new Date(), "test@email.com", 1, 2, 20, PAID);
		assertEquals("test@email.com", order.getUserEmail());
	}

	/**
	 * A test that checks if the Flower shop ID in which the order was made was added correctly
	 */
	@Test
	void testgetFlowerShopId() {
		Order order = new Order(1, new Date(), "test@email.com", 1, 2, 20, PAID);
		assertEquals(1, order.getFlowerShopId());
	}

	/**
	 * A test that checks if the amount of item in the order was added correctly
	 */
	@Test
	void testgetItemCount() {
		Order order = new Order(1, new Date(), "test@email.com", 1, 2, 20, PAID);
		assertEquals(2, order.getItemCount());
	}

	/**
	 * A test that checks if the field values of the order was added correctly
	 */
	@Test
	public void testGetFieldValues() {
		Order order = new Order(1, new Date(), "test@email.com", 1, 2, 20, PAID);
		Object[] fieldValues = order.getFieldValues();
		assertArrayEquals(new Object[]{1, Utils.formatDate(new Date()), "test@email.com", 1}, fieldValues);
	}

	/**
	 * A test that checks if the field names of the order were added correctly
	 */
	@Test
	void testgetFieldNames() {
		Order order = new Order(1, new Date(), "test@email.com", 1, 2, 20, PAID);
		String[] fieldNames = order.getFieldNames();
		assertArrayEquals(new String[]{"orderId", "orderDate", "userEmail", "flowerShopId"}, fieldNames);
	}

	/**
	 * A test that checks if the order status was added correctly
	 */
	@Test
	void testgetOrderStatus() {
		Order order = new Order(1, new Date(), "test@email.com", 1, 2, 20, PAID);
		assertEquals(PAID, order.getOrderStatus());
	}

	/**
	 * A test that checks if the price sum of the order was added correctly
	 */
	@Test
	void testgetOrderPriceSum() {
		Order order = new Order(1, new Date(), "test@email.com", 1, 2, 20, PAID);
		assertEquals(20, order.getOrderPriceSum());
	}

	/**
	 * A test that checks if the price sum of the order that is displayed was added correctly
	 */
	@Test
	void testgetOrderPriceSumAsText() {
		Order order = new Order(1, new Date(), "test@email.com", 1, 2, 20, PAID);
		assertEquals(20 + " $", order.getOrderPriceSumAsText());
	}

}