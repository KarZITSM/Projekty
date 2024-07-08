package com.aproteam.flowerpost.database;

import com.aproteam.flowerpost.exception.ItemNotFoundException;
import com.aproteam.flowerpost.exception.StoreItemNotAvailableException;
import com.aproteam.flowerpost.exception.UnknownErrorException;
import com.aproteam.flowerpost.model.FlowerType;
import com.aproteam.flowerpost.model.Order;
import com.aproteam.flowerpost.model.OrderContentElement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersContentTable {

	public static List<OrderContentElement> getOrderContent(int orderId) throws SQLException {
		List<OrderContentElement> elements = new ArrayList<>();
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("""
				SELECT orderscontentid, flowertypes.id AS flowertypeid, flowertypes.name AS flowertypename, wholesaleprice, retailprice, orderscontent.quantity, (orderscontent.quantity * retailprice) AS totalRetailPrice
				FROM orderscontent
				INNER JOIN flowertypes ON flowertypes.id = orderscontent.flowertypeid
				INNER JOIN orders ON orders.orderid = orderscontent.orderid
				WHERE orderscontent.orderid = ?""")) {
			preparedStatement.setInt(1, orderId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				FlowerType flowerType = new FlowerType(
						rs.getInt("flowertypeid"),
						rs.getString("flowertypename"),
						rs.getInt("wholesaleprice"),
						rs.getInt("retailprice")
				);
				OrderContentElement orderContentElement = new OrderContentElement(
						rs.getInt("flowertypeid"),
						flowerType,
						rs.getInt("quantity"),
						rs.getInt("totalRetailPrice"));
				elements.add(orderContentElement);
			}
		}
		return elements;
	}

	/**
	 * This function allows users to add items to their order
	 *
	 * @param order
	 * @param flowerTypeId
	 * @param quantity
	 * @throws StoreItemNotAvailableException
	 * @throws SQLException
	 */
	public static void addItemToOrder(Order order, int flowerTypeId, int quantity) throws StoreItemNotAvailableException, SQLException {
		try (Connection conn = DatabaseConnector.connect()) {
			PreparedStatement selectStatement = conn.prepareStatement("""
					SELECT quantity FROM flowershopsstock WHERE flowerTypeId = ? AND flowershopid = ?
					""");
			selectStatement.setInt(1, flowerTypeId);
			selectStatement.setInt(2, order.getFlowerShopId());
			ResultSet rs = selectStatement.executeQuery();
			if (!rs.next()) {
				throw new StoreItemNotAvailableException();
			}
			int quantityInShop = rs.getInt("quantity");
			if (quantity > quantityInShop)
				throw new StoreItemNotAvailableException();

			PreparedStatement updateStatement = conn.prepareStatement("""
					UPDATE orderscontent SET quantity = quantity + ? WHERE orderId = ? AND flowerTypeId = ?
					""");
			updateStatement.setInt(1, quantity);
			updateStatement.setInt(2, order.getOrderId());
			updateStatement.setInt(3, flowerTypeId);
			int rows = updateStatement.executeUpdate();
			if (rows <= 0) {
				PreparedStatement insertStatement = conn.prepareStatement("""
						INSERT INTO orderscontent(orderid, quantity, flowertypeid) VALUES(?, ?, ?)
						""");
				insertStatement.setInt(1, order.getOrderId());
				insertStatement.setInt(2, quantity);
				insertStatement.setInt(3, flowerTypeId);
				int rows2 = insertStatement.executeUpdate();
				if (rows2 <= 0) {
					throw new UnknownErrorException();
				}
			}
		}
	}

	/**
	 * This function allows users to delete items from their order
	 *
	 * @param order
	 * @param flowerTypeId
	 * @throws ItemNotFoundException
	 * @throws SQLException
	 */
	public static void deleteItemFromOrder(Order order, int flowerTypeId) throws ItemNotFoundException, SQLException {
		try (Connection conn = DatabaseConnector.connect()) {

			PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM orderscontent WHERE orderId = ? AND flowerTypeId = ?");
			deleteStatement.setInt(1, order.getOrderId());
			deleteStatement.setInt(2, flowerTypeId);
			int rows = deleteStatement.executeUpdate();
			if (rows <= 0) {
				throw new ItemNotFoundException(); //super("There is no such flower in your order");
			}
		}
	}


}