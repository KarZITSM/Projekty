package com.aproteam.flowerpost.ui;

import com.aproteam.flowerpost.Constants;
import com.aproteam.flowerpost.Utils;
import com.aproteam.flowerpost.database.*;
import com.aproteam.flowerpost.model.*;
import de.vandermeer.asciitable.AsciiTable;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * This class represents the creation of tables that are used to show certain information on the console
 */
public class AsciiTableGenerator {

	/**
	 * Displays menu items tree path to user
	 */
	public static void displayMenuTree(String... menuItems) {
		System.out.println(Constants.FLOWER_POST_LOGO);
		System.out.println();
		StringBuilder builder = new StringBuilder();
		for (String menuItem : menuItems) {
			if (builder.length() > 0)
				builder.append(" -> ");
			builder.append("[").append(menuItem).append("]");
		}
		AsciiTable table = new AsciiTable();
		table.addRule();
		table.addRow(builder.toString());
		table.addRule();
		System.out.println(table.render());
		System.out.println();
	}

	/**
	 * This function displays information of a logged-in user in a console
	 *
	 * @param user
	 */
	public static void displayLoggedInUserDetailsUser(User user) {
		AsciiTable table = new AsciiTable();
		table.addRule();
		table.addRow("Name", "Surname", "Email", "Permission");
		table.addRule();
		table.addRow(user.getName(), user.getSurname(), user.getEmail(), user.getPermission());
		table.addRule();
		System.out.println(table.render());
	}

	/**
	 * This function displays all users in a console
	 */
	public static void displayAllUsers() {
		try {
			AsciiTable table = new AsciiTable();
			table.addRule();
			table.addRow("Name", "Surname", "Email", "Permission");
			table.addRule();
			for (User user : UsersTable.getAllUsers()) {
				table.addRow(user.getName(), user.getSurname(), user.getEmail(), user.getPermission());
				table.addRule();
			}
			System.out.println(table.render());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function displays all flower shop that currently exist in a console
	 */
	public static void displayAllFlowerShops() {
		try {
			AsciiTable table = new AsciiTable();
			table.addRule();
			table.addRow("Id", "Name", "Address");
			table.addRule();
			for (FlowerShop flowerShop : FlowerShopsTable.getAllFlowerShops()) {
				table.addRow(flowerShop.getId(), flowerShop.getName(), flowerShop.getAddress());
				table.addRule();
			}
			System.out.println(table.render());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function displays the stock of all flowers shops that currently exist in a console
	 *
	 * @param flowerShopId
	 */
	public static void displayAllFlowerShopStock(int flowerShopId) {
		try {
			AsciiTable table = new AsciiTable();
			Map<FlowerType, Integer> flowerShopStock = FlowerShopsStockTable.getFlowerShopStock(flowerShopId);
			table.addRule();
			table.addRow("Flower Type Id", "Name", "Quantity", "Price");
			table.addRule();
			for (FlowerType flowerType : flowerShopStock.keySet()) {
				int quantity = flowerShopStock.get(flowerType);
				table.addRow(flowerType.getId(), flowerType.getName(), quantity, flowerType.getRetailPrice() + " $");
				table.addRule();
			}
			System.out.println(table.render());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function displays the contents of users order in a console
	 *
	 * @param order
	 */
	public static void displayOrderContent(Order order) {
		try {
			AsciiTable table = new AsciiTable();
			List<OrderContentElement> orderContentElements = OrdersContentTable.getOrderContent(order.getOrderId());
			if (orderContentElements.isEmpty()) {
				System.out.println("Your order is empty. Please add some items.");
				return;
			}
			table.addRule();
			table.addRow("Id", "Item name", "Quantity", "Price per item", "Total price");
			table.addRule();
			for (OrderContentElement orderContentElement : orderContentElements) {
				FlowerType flowerType = (FlowerType) orderContentElement.getStoreItem();
				table.addRow(
						orderContentElement.getId(),
						flowerType.getName(),
						orderContentElement.getQuantity(),
						flowerType.getRetailPrice() + " $",
						orderContentElement.getTotalRetailPrice() + " $"
				);
				table.addRule();
			}
			System.out.println(table.render());
			System.out.println("Order sum price: " + order.getOrderPriceSumAsText());
			System.out.println("Order status:");
			System.out.println("|" + "=".repeat(80 * (order.getOrderStatus().ordinal() + 1) / (OrderStatus.values().length + 2)) + "> " + order.getOrderStatus());
			System.out.println();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function displays the order to user in a console
	 *
	 * @param user
	 */
	public static void displayOrdersForUser(User user) {
		try {
			AsciiTable table = new AsciiTable();
			List<Order> orders = OrdersTable.findOrdersByForUser(user);
			if (orders.isEmpty()) {
				System.out.println("You do not have any orders yet.");
				return;
			}
			table.addRule();
			table.addRow(
					"Order id",
					"Order date",
					"Flower shop id",
					"Item count",
					"orderPriceSum",
					"Order status");
			table.addRule();
			for (Order order : orders) {
				table.addRow(
						order.getOrderId(),
						Utils.formatDate(order.getOrderDate()),
						order.getFlowerShopId(),
						order.getItemCount(),
						order.getOrderPriceSumAsText(),
						order.getOrderStatus()
				);
				table.addRule();
			}
			System.out.println(table.render());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function displays all orders for the admin in a console
	 */
	public static void displayAllOrders() {
		try {
			AsciiTable table = new AsciiTable();
			List<Order> orders = OrdersTable.getAllOrders();
			if (orders.isEmpty()) {
				System.out.println("There are no orders yet.");
				return;
			}
			table.addRule();
			table.addRow(
					"Order id",
					"User",
					"Order date",
					"Flower shop id",
					"Item count",
					"Price sum",
					"Order status");
			table.addRule();
			for (Order order : orders) {
				table.addRow(
						order.getOrderId(),
						order.getUserEmail(),
						Utils.formatDate(order.getOrderDate()),
						order.getFlowerShopId(),
						order.getItemCount(),
						order.getOrderPriceSumAsText(),
						order.getOrderStatus()
				);
				table.addRule();
			}
			System.out.println(table.render());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function displays all current flower types in a console
	 */
	public static void displayAllFlowerTypes() {
		try {
			AsciiTable table = new AsciiTable();
			table.addRule();
			table.addRow("Id", "Name", "Wholesale Price", "Retail price");
			table.addRule();
			for (FlowerType flowerType : FlowerTypesTable.getAllFlowerTypes()) {
				table.addRow(flowerType.getId(), flowerType.getName(), flowerType.getWholesalePrice() + " $", flowerType.getRetailPrice() + " $");
				table.addRule();
			}
			System.out.println(table.render());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}