import com.aproteam.flowerpost.model.Permission;
import com.aproteam.flowerpost.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest {
	/**
	 * A test that checks if the user data was added correctly
	 */
	@Test
	public void testConstructor() {
		Permission permission = Permission.USER;
		User user = new User("Jan", "Kowalski", "mail@gmail.com", permission);
		assertEquals("Jan", user.getName());
		assertEquals("Kowalski", user.getSurname());
		assertEquals("mail@gmail.com", user.getEmail());
		assertEquals(permission, user.getPermission());
	}

	/**
	 * A test that checks if the function that displays name and a surname of a user works correctly
	 */
	@Test
	public void testGetFullName() {
		Permission permission = Permission.USER;
		User user = new User("Jan", "Kowalski", "mail@gmail.com", permission);
		assertEquals("Jan Kowalski", user.getFullName());

	}

	/**
	 * A test that checks if the field values of user data were added correctly
	 */
	@Test
	void testgetFieldValues() {
		Permission permission = Permission.USER;
		User user = new User("Jan", "Kowalski", "mail@gmail.com", permission);
		Object[] expected = new Object[]{"Jan", "Kowalski", "mail@gmail.com", permission};
		assertArrayEquals(expected, user.getFieldValues());
	}

	/**
	 * A test that checks if the field names of the user data were added correctly
	 */
	@Test
	public void testGetFieldNames() {
		String[] expected = new String[]{"name", "surname", "email", "permission"};
		assertArrayEquals(expected, User.getFieldNames());
	}

}