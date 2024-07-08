import com.aproteam.flowerpost.model.FlowerShop;
import com.aproteam.flowerpost.model.FlowerType;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FlowerShopTest {

	/**
	 * A test that checks if the ID of a new flower shop was added correctly
	 */
	@Test
	public void testGetId() {
		FlowerShop flowerShop = new FlowerShop(1, "Rose Garden", "123 Main St");
		assertEquals(1, flowerShop.getId());
	}

	/**
	 * A test that checks if the name of a new flower shop was added correctly
	 */
	@Test
	public void testGetName() {
		FlowerShop flowerShop = new FlowerShop(1, "Rose Garden", "123 Main St");
		assertEquals("Rose Garden", flowerShop.getName());
	}

	/**
	 * A test that checks if the address of a new flower shop was added correctly
	 */
	@Test
	public void testGetAddress() {
		FlowerShop flowerShop = new FlowerShop(1, "Rose Garden", "123 Main St");
		assertEquals("123 Main St", flowerShop.getAddress());
	}

	/**
	 * A test that checks if the new flower types are added correctly
	 */
	@Test
	public void testGetFlowers() {
		FlowerShop flowerShop = new FlowerShop(1, "Rose Garden", "123 Main St");
		Map<FlowerType, Integer> flowers = flowerShop.getFlowers();
		assertNotNull(flowers);
		assertTrue(flowers.isEmpty());
	}

	/**
	 * A test that checks if the field values of a new flower shop were added correctly
	 */
	@Test
	public void testGetFieldValues() {
		FlowerShop flowerShop = new FlowerShop(1, "Rose Garden", "123 Main St");
		Object[] fieldValues = flowerShop.getFieldValues();
		assertArrayEquals(new Object[]{1, "Rose Garden", "123 Main St"}, fieldValues);
	}

	/**
	 * A test that checks if the field names of flower shops are correct
	 */
	@Test
	public void testGetFieldNames() {
		String[] fieldNames = FlowerShop.getFieldNames();
		assertArrayEquals(new String[]{"id", "name", "address"}, fieldNames);
	}

}