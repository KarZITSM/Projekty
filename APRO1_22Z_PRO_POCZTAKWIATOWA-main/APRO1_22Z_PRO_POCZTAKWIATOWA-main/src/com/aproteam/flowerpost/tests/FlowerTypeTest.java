import com.aproteam.flowerpost.model.FlowerType;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class FlowerTypeTest {

	/**
	 * A test that checks if the ID of a new flower type was added correctly
	 */
	@Test
	public void testGetId() {
		FlowerType flowerType = new FlowerType(1, "Rose", 5, 10);
		assertEquals(1, flowerType.getId());
	}

	/**
	 * A test that checks if the name of a new flower type was added correctly
	 */
	@Test
	public void testGetName() {
		FlowerType flowerType = new FlowerType(1, "Rose", 5, 10);
		assertEquals("Rose", flowerType.getName());
	}

	/**
	 * A test that checks if the wholesale price of a new flower type was added correctly
	 */
	@Test
	public void testGetWholesalePrice() {
		FlowerType flowerType = new FlowerType(1, "Rose", 5, 10);
		assertEquals(5, flowerType.getWholesalePrice());
	}

	/**
	 * A test that checks if the retail price of a new flower type was added correctly
	 */
	@Test
	public void testGetRetailPrice() {
		FlowerType flowerType = new FlowerType(1, "Rose", 5, 10);
		assertEquals(10, flowerType.getRetailPrice());
	}

	/**
	 * A test that checks if the field values of a new flower type were added correctly
	 */
	@Test
	public void testGetFieldValues() {
		FlowerType flowerType = new FlowerType(1, "Rose", 5, 10);
		Object[] fieldValues = flowerType.getFieldValues();
		assertArrayEquals(new Object[]{1, "Rose", "5 $", "10 $"}, fieldValues);
	}

	/**
	 * A test that checks if the field names of a flower type are correct
	 */
	@Test
	public void testGetFieldNames() {
		String[] fieldNames = FlowerType.getFieldNames();
		assertArrayEquals(new String[]{"Id", "Name", "Wholesale Price", "Retail price"}, fieldNames);
	}

	/**
	 * A test that checks equality of flower types
	 */
	@Test
	public void testEquals() {
		FlowerType flowerType1 = new FlowerType(1, "Rose", 5, 10);
		FlowerType flowerType2 = new FlowerType(1, "Rose", 5, 10);
		assertTrue(flowerType1.equals(flowerType2));
	}

	/**
	 * A test that checks if the class hashCode works correctly
	 */
	@Test
	public void testHashCode() {
		FlowerType flowerType = new FlowerType(1, "Rose", 5, 10);
		assertEquals(Objects.hash(1), flowerType.hashCode());
	}

	/**
	 * A test that checks if the class toString works correctly
	 */
	@Test
	public void testToString() {
		FlowerType flowerType = new FlowerType(1, "Rose", 5, 10);
		assertEquals("Rose", flowerType.toString());
	}

}