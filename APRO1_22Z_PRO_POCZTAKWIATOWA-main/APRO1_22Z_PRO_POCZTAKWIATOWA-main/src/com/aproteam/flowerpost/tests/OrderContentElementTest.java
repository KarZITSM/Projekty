import com.aproteam.flowerpost.model.FlowerType;
import com.aproteam.flowerpost.model.OrderContentElement;
import com.aproteam.flowerpost.model.StoreItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderContentElementTest {

	/**
	 * A test that checks if the ID of a new element of the order was added correctly
	 */
	@Test
	public void testGetId() {
		StoreItem storeItem = new FlowerType(1, "Rose", 5, 10);
		OrderContentElement element = new OrderContentElement(1, storeItem, 2, 20);
		assertEquals(1, element.getId());
	}

	/**
	 * A test that checks if the new element of the order was added correctly
	 */
	@Test
	public void testGetStoreItem() {
		StoreItem storeItem = new FlowerType(1, "Rose", 5, 10);
		OrderContentElement element = new OrderContentElement(1, storeItem, 2, 20);
		assertEquals(storeItem, element.getStoreItem());
	}

	/**
	 * A test that checks if the field values of the new element of the order was added correctly
	 */
	@Test
	public void testGetFieldValues() {
		StoreItem storeItem = new FlowerType(1, "Rose", 5, 10);
		OrderContentElement element = new OrderContentElement(1, storeItem, 2, 20);
		Object[] expected = new Object[]{1, storeItem, 2};
		assertArrayEquals(expected, element.getFieldValues());
	}

	/**
	 * A test that checks if the field names of the new element of the order was added correctly
	 */
	@Test
	public void testGetFieldNames() {
		String[] expected = new String[]{"id", "storeItem", "quantity"};
		assertArrayEquals(expected, OrderContentElement.getFieldNames());
	}

	/**
	 * A test that checks if the quantity of the new element of the order was added correctly
	 */
	@Test
	public void testGetQuantity() {
		StoreItem storeItem = new FlowerType(1, "Rose", 5, 10);
		OrderContentElement element = new OrderContentElement(1, storeItem, 2, 20);
		assertEquals(2, element.getQuantity());
	}

	/**
	 * A test that checks if the total retail price of the new element of the order was added correctly
	 */
	@Test
	public void testGetTotalRetailPrice() {
		StoreItem storeItem = new FlowerType(1, "Rose", 5, 10);
		OrderContentElement element = new OrderContentElement(1, storeItem, 2, 20);
		assertEquals(20, element.getTotalRetailPrice());
	}

}