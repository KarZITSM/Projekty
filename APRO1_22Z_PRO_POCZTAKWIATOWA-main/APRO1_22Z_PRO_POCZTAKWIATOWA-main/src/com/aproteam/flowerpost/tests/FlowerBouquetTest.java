import com.aproteam.flowerpost.model.FlowerBouquet;
import com.aproteam.flowerpost.model.FlowerType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FlowerBouquetTest {

	/**
	 * A test that checks if the bouquets are created correctly
	 */
	@Test
	void testgetFlowers() {
		List<FlowerType> flowers = new ArrayList<>();
		flowers.add(new FlowerType(1, "Rose", 10, 7));
		flowers.add(new FlowerType(2, "Tulip", 9, 6));
		FlowerBouquet bouquet = new FlowerBouquet(flowers);
		List<FlowerType> bouquetFlowers = bouquet.getFlowers();
		assertNotNull(bouquetFlowers);
		assertEquals(flowers, bouquetFlowers);
	}

}