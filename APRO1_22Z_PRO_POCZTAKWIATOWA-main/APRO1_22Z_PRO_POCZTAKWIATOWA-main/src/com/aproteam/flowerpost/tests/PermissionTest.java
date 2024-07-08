import com.aproteam.flowerpost.model.Permission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PermissionTest {
	/**
	 * Tests that check if the permission status of user/admin is interpreted correctly
	 */
	@Test
	void testValues() {
		Permission[] expected = {Permission.USER, Permission.ADMIN};
		assertArrayEquals(expected, Permission.values());
	}

	@Test
	void testValueOf() {
		assertEquals(Permission.USER, Permission.valueOf("USER"));
	}

}