package com.aproteam.flowerpost.ui;

import com.aproteam.flowerpost.Constants;
import com.aproteam.flowerpost.FlowerPostSystem;
import com.aproteam.flowerpost.PaymentSystemSimulator;
import com.aproteam.flowerpost.Utils;
import com.aproteam.flowerpost.database.*;
import com.aproteam.flowerpost.exception.*;
import com.aproteam.flowerpost.model.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class allows users to inetract with the whole flower shop system through the console
 */
public class UserInterfaceOfFlowerShopSystem {

	private final FlowerPostSystem flowerPostSystem;
	private final Scanner scanner;

	/**
	 * The constructor of FlowerPostSystem class
	 *
	 * @param flowerPostSystem
	 */
	public UserInterfaceOfFlowerShopSystem(FlowerPostSystem flowerPostSystem) {
		this.flowerPostSystem = flowerPostSystem;
		this.scanner = new Scanner(System.in);
	}

	@SuppressWarnings("InfiniteLoopStatement")
	public void mainMenu() {
		while (true) {
			if (flowerPostSystem.getLoggedInUser() == null) {
				/*NOT LOGGED IN YET*/
				notLoggedInMainMenu();
			} else if (flowerPostSystem.getLoggedInUser().getPermission() == Permission.ADMIN) {
				/*USER IS LOGGED IN AS ADMIN*/
				adminMainMenu();
			} else {
				/*USER IS LOGGED IN AS NOT ADMIN*/
				loggedInMainMenu();
			}
		}
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void notLoggedInMainMenu() {
		Utils.clearOutputConsole();
		AsciiTableGenerator.displayMenuTree("Main menu");
		System.out.println("""
				1. Login
				2. Create new account
				0. Exit
				""");
		System.out.print("Enter your choice: ");
		switch (promptForInt()) {
			case 1 -> loginUser();
			case 2 -> createUser();
			case 0 -> exit();
			default -> printAndWaitForEnter("Invalid choice. Please try again.");
		}
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void adminMainMenu() {
		Utils.clearOutputConsole();
		AsciiTableGenerator.displayMenuTree("Main menu");
		System.out.printf("""
				You are now logged in as %s (ADMIN).
				1. Manage users
				2. Manage flower shops
				3. Manage flower types
				4. Manage orders
				9. Logout
				0. Exit
				""", flowerPostSystem.getLoggedInUser().getFullName());
		System.out.print("Enter your choice: ");
		switch (promptForInt()) {
			case 1 -> manageUsers();
			case 2 -> manageFlowerShops();
			case 3 -> manageFlowerTypes();
			case 4 -> manageOrders();
			case 9 -> logoutUser();
			case 0 -> exit();
			default -> printAndWaitForEnter("Invalid choice. Please try again.");
		}
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void loggedInMainMenu() {
		Utils.clearOutputConsole();
		AsciiTableGenerator.displayMenuTree("Main menu");
		System.out.printf("""
				You are now logged in as %s.
				1. View user details
				2. Make an order
				3. Manage my orders
				9. Logout
				0. Exit
				""", flowerPostSystem.getLoggedInUser().getFullName());
		System.out.print("Enter your choice: ");
		switch (promptForInt()) {
			case 1 -> displayUserDetails();
			case 2 -> makeAnOrder();
			case 3 -> manageUserOrders();
			case 9 -> logoutUser();
			case 0 -> exit();
			default -> printAndWaitForEnter("Invalid choice. Please try again.");
		}
	}

	/**
	 * This function exits the system
	 */
	public void exit() {
		System.out.println("Exiting program.");
		System.exit(0);
	}

	/**
	 * This function allows users to create a new account through a console
	 */
	public void createUser() {
		System.out.print("Enter name: ");
		String name = scanner.nextLine();

		System.out.print("Enter surname: ");
		String surname = scanner.nextLine();

		String email;
		while (true) {
			System.out.print("Enter email: ");
			email = scanner.nextLine();
			if (email.matches(Constants.EMAIL_REGEX)) {
				break;
			}
			System.out.println("Not valid format of email address");
		}

		System.out.print("Enter password: ");
		String password = Utils.promptForPasswordInput();

		try {
			boolean isThereAnyUser = UsersTable.getAllUsersCount() > 0;
			UsersTable.createNewUser(name, surname, email, password, isThereAnyUser ? Permission.USER : Permission.ADMIN);
			printAndWaitForEnter("User created!");
		} catch (UserAlreadyExistsException e) {
			printAndWaitForEnter(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			printAndWaitForEnter("");
		}
	}

	/**
	 * This function allows users to log in their account through a console
	 */
	public void loginUser() {
		System.out.print("Enter email: ");
		String email = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = Utils.promptForPasswordInput();
		try {
			flowerPostSystem.setLoggedInUser(UsersTable.loginUser(email, password));
		} catch (UserNotFoundException e) {
			printAndWaitForEnter("Invalid login credentials");
		} catch (SQLException e) {
			e.printStackTrace();
			printAndWaitForEnter("");
		}
	}

	/**
	 * This function logs out users
	 */
	public void logoutUser() {
		flowerPostSystem.setLoggedInUser(null);
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void manageUsers() {
		while (true) {
			Utils.clearOutputConsole();
			AsciiTableGenerator.displayMenuTree("Main menu", "Manage Users");
			AsciiTableGenerator.displayAllUsers();
			System.out.println("""
					1. Create new user
					0. Back
					""");
			System.out.print("Enter your choice: ");
			switch (promptForInt()) {
				case 1 -> createUser();
				case 0 -> {
					return;
				}
				default -> printAndWaitForEnter("Invalid choice");
			}
		}
	}

	/**
	 * This function allows users to display their account details through a console
	 */
	public void displayUserDetails() {
		while (true) {
			Utils.clearOutputConsole();
			AsciiTableGenerator.displayMenuTree("Main menu", "User Details");
			AsciiTableGenerator.displayLoggedInUserDetailsUser(flowerPostSystem.getLoggedInUser());
			System.out.println("""
					0. Back
					""");
			System.out.print("Enter your choice: ");
			switch (promptForInt()) {
				case 0 -> {
					return;
				}
			}
		}
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void manageUserOrders() {
		while (true) {
			Utils.clearOutputConsole();
			AsciiTableGenerator.displayMenuTree("Main menu", "Your orders");
			AsciiTableGenerator.displayOrdersForUser(flowerPostSystem.getLoggedInUser());
			System.out.println("""
					1. View order details
					0. Back
					""");
			System.out.print("Enter your choice: ");
			switch (promptForInt()) {
				case 1 -> {
					System.out.print("Enter id of the order you want to view: ");
					int orderId = promptForInt();
					viewOrder(orderId);
				}
				case 0 -> {
					return;
				}
				default -> printAndWaitForEnter("Invalid choice");
			}
		}
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void manageFlowerShops() {
		while (true) {
			Utils.clearOutputConsole();
			AsciiTableGenerator.displayMenuTree("Main menu", "Manage Flower Shops");
			AsciiTableGenerator.displayAllFlowerShops();
			System.out.println("""
					1. Create new shop
					2. Manage shop stock
					0. Back
					""");
			System.out.print("Enter your choice: ");
			switch (promptForInt()) {
				case 1 -> createFlowerShop();
				case 2 -> {
					System.out.print("Enter shop id: ");
					int flowerShopId = promptForInt();
					try {
						FlowerShop flowerShop = FlowerShopsTable.findFlowerShopById(flowerShopId);
						manageFlowerShopStock(flowerShop);
					} catch (FlowerShopNotFoundException e) {
						System.out.println("Shop with this id does not exist. Press Enter to continue.");
						scanner.nextLine();
					} catch (SQLException e) {
						e.printStackTrace();
						printAndWaitForEnter("");
					}
				}
				case 0 -> {
					return;
				}
				default -> printAndWaitForEnter("Invalid choice, try again");
			}
		}
	}

	/**
	 * This function allows admins to create a new flower shop through a console
	 */
	public void createFlowerShop() {
		System.out.print("Enter name: ");
		String name = scanner.nextLine();
		System.out.print("Enter address: ");
		String address = scanner.nextLine();
		try {
			FlowerShop flowerShop = FlowerShopsTable.createNewFlowerShop(name, address);
			System.out.println("Flower Shop created!");
		} catch (SQLException e) {
			e.printStackTrace();
			printAndWaitForEnter("");
		}
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void manageFlowerShopStock(FlowerShop flowerShop) {
		while (true) {
			Utils.clearOutputConsole();
			AsciiTableGenerator.displayMenuTree(
					"Main menu",
					"Manage Flower Shops",
					String.format("Manage stock of %s", flowerShop.getName())
			);
			AsciiTableGenerator.displayAllFlowerShopStock(flowerShop.getId());
			System.out.println("""
					1. Add to stock
					0. Back
					""");
			System.out.print("Enter your choice: ");
			switch (promptForInt()) {
				case 1 -> updateFlowerShopStock(flowerShop);
				case 0 -> {
					return;
				}
				default -> printAndWaitForEnter("Invalid choice, try again");
			}
		}
	}

	/**
	 * This function allows admins to update the stock of existing flower shops through a console
	 */
	public void updateFlowerShopStock(FlowerShop flowerShop) {
		Utils.clearOutputConsole();
		AsciiTableGenerator.displayMenuTree(
				"Main menu",
				"Manage Flower Shops",
				String.format("Add stock of %s", flowerShop.getName())
		);
		AsciiTableGenerator.displayAllFlowerTypes();
		System.out.print("Enter flower type id: ");
		int flowerTypeId = promptForInt();
		System.out.print("Enter quantity: ");
		int quantity = promptForInt();
		try {
			FlowerShopsStockTable.addToFlowerShopStock(flowerShop.getId(), flowerTypeId, quantity);
			System.out.println("Shop's stock successfully updated!");
		} catch (SQLException e) {
			e.printStackTrace();
			printAndWaitForEnter("");
		}
	}

	/**
	 * This function allows users to create a bouquet through a console
	 */
	public HashMap<Integer, Integer> createBouquet(FlowerShop flowerShop) {
		Scanner scanner = new Scanner(System.in);
		HashMap<Integer, Integer> bouquet = new HashMap<>();
		System.out.println("Here are available flower types: ");
		AsciiTableGenerator.displayAllFlowerShopStock(flowerShop.getId());
		while (true) {
			System.out.print("Choose flowers for your bouquet by entering its id");
			System.out.println("Enter '0' if the bouquet is done or you want to leave this function");
			int input = promptForInt();
			if (input == 0) {
				break;
			}
			System.out.print("Enter quantity of the flower type ");
			int amount = promptForInt();
			bouquet.put(input, amount);
		}
		return bouquet;
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void manageOrders() {
		while (true) {
			Utils.clearOutputConsole();
			AsciiTableGenerator.displayMenuTree(
					"Main menu",
					"Manage Orders"
			);
			AsciiTableGenerator.displayAllOrders();
			System.out.println("""
					1. View order details
					0. Back
					""");
			System.out.print("Enter your choice: ");
			switch (promptForInt()) {
				case 1 -> {
					System.out.print("Enter id of the order you want to view: ");
					int orderId = promptForInt();
					viewOrder(orderId);
				}
				case 0 -> {
					return;
				}
				default -> printAndWaitForEnter("Invalid choice");
			}
		}
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void manageFlowerTypes() {
		while (true) {
			Utils.clearOutputConsole();
			AsciiTableGenerator.displayMenuTree(
					"Main menu",
					"Manage Flower Types"
			);
			AsciiTableGenerator.displayAllFlowerTypes();
			System.out.println("""
					1. Create new flower type
					0. Back
					""");
			System.out.print("Enter your choice: ");
			switch (promptForInt()) {
				case 1 -> createFlowerType();
				case 0 -> {
					return;
				}
			}
		}
	}

	/**
	 * This function allows admins to create a new flower type through a console
	 */
	public void createFlowerType() {
		System.out.print("Enter name: ");
		String name = scanner.nextLine();
		System.out.print("Enter wholesale price: ");
		int wholesalePrice = promptForInt();
		System.out.print("Enter retail price: ");
		int retailPrice = promptForInt();
		try {
			FlowerType flowerType = FlowerTypesTable.createNewFlowerType(name, wholesalePrice, retailPrice);
			System.out.println("Flower Type created!");
		} catch (SQLException e) {
			e.printStackTrace();
			printAndWaitForEnter("");
		}
	}

	/**
	 * This function prompts users to input integers when it's required in the console
	 *
	 * @return
	 */
	public static int promptForInt() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {
				String input = scanner.nextLine().trim();
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("Please input an integer.");
			}
		}
	}

	/**
	 * This function displays a message to click enter in order to move forward in the console
	 *
	 * @param message
	 */
	public static void printAndWaitForEnter(String message) {
		System.out.println(message + " Press Enter to continue.");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}

	/**
	 * This function allows users to make an order through a console
	 */
	public void makeAnOrder() {
		Utils.clearOutputConsole();
		AsciiTableGenerator.displayMenuTree(
				"Main menu",
				"Make an order"
		);
		AsciiTableGenerator.displayAllFlowerShops();
		System.out.print("Enter shop id (or 0 to go back): ");
		int flowerShopId = promptForInt();
		if (flowerShopId == 0) {
			return;
		}
		FlowerShop flowerShop;
		try {
			flowerShop = FlowerShopsTable.findFlowerShopById(flowerShopId);
		} catch (FlowerShopNotFoundException e) {
			printAndWaitForEnter("Shop with this id does not exist.");
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			printAndWaitForEnter("");
			return;
		}
		Order order;
		try {
			order = OrdersTable.createNewOrder(flowerPostSystem.getLoggedInUser(), flowerShop);
		} catch (SQLException e) {
			e.printStackTrace();
			printAndWaitForEnter("");
			return;
		}
		viewOrder(order.getOrderId());
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void viewOrder(int orderId) {
		while (true) {
			Order order;
			try {
				order = OrdersTable.findOrderById(flowerPostSystem.getLoggedInUser(), orderId);
			} catch (OrderNotFoundException e) {
				printAndWaitForEnter("The order with this id does not exist.");
				return;
			} catch (SQLException e) {
				e.printStackTrace();
				printAndWaitForEnter("");
				return;
			}
			Utils.clearOutputConsole();
			AsciiTableGenerator.displayMenuTree(
					"Main menu",
					"Your orders",
					String.format("Your order no. %d", order.getOrderId())
			);
			AsciiTableGenerator.displayOrderContent(order);
			if (order.getOrderStatus() != OrderStatus.CREATED) {
				System.out.println("""
						0. Back
						""");
				System.out.print("Enter your choice: ");
				switch (promptForInt()) {
					case 0 -> {
						return;
					}
				}
			} else {
				System.out.println("""
						1. Add new item
						2. Delete item
						3. Pay for order
						0. Back
						""");
				System.out.print("Enter your choice: ");
				switch (promptForInt()) {
					case 1 -> {
						addItemsToOrder(order);
					}
					case 2 -> deleteItemsFromOrder(order);
					case 3 -> {
						if (order.getItemCount() == 0) {
							printAndWaitForEnter("Please add some items to your order first.");
							continue;
						}
						boolean wasPaid = payForOrder(order);
						if (wasPaid)
							return;
					}
					case 0 -> {
						return;
					}
				}
			}
		}
	}

	/**
	 * This function allows users to delete specified items from their order through a console
	 */
	public void deleteItemsFromOrder(Order order) {
		System.out.print("Enter flower type id: ");
		int flowerTypeId = promptForInt();
		try {
			OrdersContentTable.deleteItemFromOrder(order, flowerTypeId);
		} catch (ItemNotFoundException e) {
			printAndWaitForEnter(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			printAndWaitForEnter("");
		}
	}

	/**
	 * This function allows users to add new items to the order through a console
	 */
	public void addItemsToOrder(Order order) {
		System.out.println("Here are available flower types: ");
		AsciiTableGenerator.displayAllFlowerShopStock(order.getFlowerShopId());
		System.out.print("Enter flower type id: ");
		int flowerTypeId = promptForInt();
		System.out.print("Enter quantity: ");
		int quantity = promptForInt();
		try {
			OrdersContentTable.addItemToOrder(order, flowerTypeId, quantity);
		} catch (StoreItemNotAvailableException e) {
			printAndWaitForEnter(e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			printAndWaitForEnter("");
		}
	}

	/**
	 * This function allows user to for the order through a console
	 *
	 * @return true if the order was paid, false otherwise
	 */
	public boolean payForOrder(Order order) {
		while (true) {
			Utils.clearOutputConsole();
			AsciiTableGenerator.displayMenuTree(
					"Main menu",
					"Your orders",
					String.format("Your order no. %d", order.getOrderId()),
					"Payment"
			);
			AsciiTableGenerator.displayOrderContent(order);
			System.out.println("Choose a payment method:");
			System.out.println("""
					1. BLIK code
					2. Bank Transfer
					3. Cash on delivery
					0. Back
					""");
			boolean paymentResult = false;
			System.out.print("Enter your choice: ");
			switch (promptForInt()) {
				case 1 -> {
					System.out.print("Enter BLIK code: ");
					String blikCode = scanner.nextLine();
					if (!blikCode.matches("[0-9]{6}")) {
						printAndWaitForEnter("BLIK code should be in 6-digit format.");
						continue;
					}
					paymentResult = PaymentSystemSimulator.payWithBLIK(blikCode);
				}
				case 2 -> paymentResult = PaymentSystemSimulator.payWithBankTransfer();
				case 3 -> paymentResult = true;
				case 0 -> {
					return false;
				}
			}
			if (paymentResult) {
				try {
					FlowerShopsStockTable.updateFlowerShopStockAfterOrderPaid(order);
				} catch (SQLException e) {
					e.printStackTrace();
					printAndWaitForEnter("");
				}
				try {
					OrdersTable.updateOrderStatus(order.getUserEmail(), order.getOrderId(), OrderStatus.PAID);
				} catch (OrderNotFoundException e) {
					printAndWaitForEnter(e.getMessage());
				} catch (SQLException e) {
					e.printStackTrace();
					printAndWaitForEnter("");
				}
				printAndWaitForEnter("Order was successfully paid.");
				PaymentSystemSimulator.simulateOrderStatusChange(order);
				return true;
			} else {
				printAndWaitForEnter("An error occurred during the payment process.");
			}
		}
	}

	/**
	 * This function allows users to choose certain actions that are displayed on the console by typing in number that corresponds with the wanted action.
	 * Then this function initiates next function that either takes the user to the next set of options or executes wanted action.
	 */
	public void orderPlacement(FlowerShop flowerShop) {
		Utils.clearOutputConsole();
		System.out.println("""
				1. Pre made bouquets.
				2. Create your bouquet
				0. Back
				""");
		switch (promptForInt()) {
			case 1 -> printAndWaitForEnter("There is no pre made bouquets yet");
			case 2 -> createBouquet(flowerShop);
			case 0 -> {
				return;
			}
			default -> printAndWaitForEnter("Invalid choice, try again");
		}
	}

}