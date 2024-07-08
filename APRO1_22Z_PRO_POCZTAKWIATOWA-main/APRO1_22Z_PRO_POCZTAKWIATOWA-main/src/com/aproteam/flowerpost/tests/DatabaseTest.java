import com.aproteam.flowerpost.database.DatabaseConnector;
import com.aproteam.flowerpost.database.FlowerShopsTable;
import com.aproteam.flowerpost.database.FlowerTypesTable;
import com.aproteam.flowerpost.database.UsersTable;
import com.aproteam.flowerpost.model.FlowerShop;
import com.aproteam.flowerpost.model.FlowerType;
import com.aproteam.flowerpost.model.Permission;
import com.aproteam.flowerpost.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DatabaseTest {

	public DatabaseTest() {
		DatabaseConnector.DATABASE_FILENAME = "flowerpost_test.db";
	}

	private static final String ADMIN_NAME = "Admin";
	private static final String ADMIN_SURNAME = "Adminowski";
	private static final String ADMIN_EMAIL = "admin@pw.edu.pl";
	private static final String ADMIN_PASSWORD = "09876543212kop";

	private static final String USER_NAME = "Adam";
	private static final String USER_SURNAME = "Adamski";
	private static final String USER_EMAIL = "adam@gmail.com";
	private static final String USER_PASSWORD = "qwerty098755";

	User adminUser, normalUser;

	@Test
	void testRefreshDatabase() {
		Assertions.assertDoesNotThrow(() -> {
			DatabaseConnector.dropAllTables();
			DatabaseConnector.createTablesIfNeeded();
		});
	}

	@Test
	void testCreateNewAdminUser() {
		Assertions.assertDoesNotThrow(() -> {
			int allUsersCount = UsersTable.getAllUsersCount();
			adminUser = UsersTable.createNewUser(ADMIN_NAME, ADMIN_SURNAME, ADMIN_EMAIL, ADMIN_PASSWORD, Permission.ADMIN);
			Assertions.assertNotNull(adminUser);
			Assertions.assertEquals(allUsersCount + 1, UsersTable.getAllUsersCount());
		});
	}

	@Test
	void testCreateNewUser() {
		Assertions.assertDoesNotThrow(() -> {
			int allUsersCount = UsersTable.getAllUsersCount();
			normalUser = UsersTable.createNewUser(USER_NAME, USER_SURNAME, USER_EMAIL, USER_PASSWORD, Permission.USER);
			Assertions.assertNotNull(normalUser);
			Assertions.assertEquals(allUsersCount + 1, UsersTable.getAllUsersCount());
		});
	}

	@Test
	void testFlowerShopCreation() {
		Assertions.assertDoesNotThrow(() -> {
			List<FlowerShop> allFlowerShops = FlowerShopsTable.getAllFlowerShops();
			FlowerShop createdFlowerShop = FlowerShopsTable.createNewFlowerShop("TEST FLOWER SHOP", "TEST FLOWER SHOP ADDRESS");
			Assertions.assertNotNull(createdFlowerShop);
			List<FlowerShop> allFlowerShopsAfterAddition = FlowerShopsTable.getAllFlowerShops();
			Assertions.assertEquals(allFlowerShops.size() + 1, allFlowerShopsAfterAddition.size());
			FlowerShop flowerShopById = FlowerShopsTable.findFlowerShopById(createdFlowerShop.getId());
			Assertions.assertNotNull(flowerShopById);
			Assertions.assertEquals(createdFlowerShop, flowerShopById);
		});
	}

	@Test
	void testFlowerTypeCreation() {
		Assertions.assertDoesNotThrow(() -> {
			List<FlowerType> allFlowerTypes = FlowerTypesTable.getAllFlowerTypes();
			FlowerType createdFlowerType = FlowerTypesTable.createNewFlowerType("TEST FLOWER", 5, 6);
			Assertions.assertNotNull(createdFlowerType);
			List<FlowerType> allFlowerTypesAfterAddition = FlowerTypesTable.getAllFlowerTypes();
			Assertions.assertEquals(allFlowerTypes.size() + 1, allFlowerTypesAfterAddition.size());
		});
	}

	@Test
	void testMakeAnOrder() {
		Assertions.assertDoesNotThrow(() -> {
			List<FlowerType> allFlowerTypes = FlowerTypesTable.getAllFlowerTypes();
			FlowerType createdFlowerType = FlowerTypesTable.createNewFlowerType("TEST FLOWER", 5, 6);
			Assertions.assertNotNull(createdFlowerType);
			List<FlowerType> allFlowerTypesAfterAddition = FlowerTypesTable.getAllFlowerTypes();
			Assertions.assertEquals(allFlowerTypes.size() + 1, allFlowerTypesAfterAddition.size());
		});
	}

}