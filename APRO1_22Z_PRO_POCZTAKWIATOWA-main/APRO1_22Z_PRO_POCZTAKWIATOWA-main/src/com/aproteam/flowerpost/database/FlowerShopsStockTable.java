package com.aproteam.flowerpost.database;

import com.aproteam.flowerpost.exception.UnknownErrorException;
import com.aproteam.flowerpost.model.FlowerType;
import com.aproteam.flowerpost.model.Order;
import com.aproteam.flowerpost.model.OrderContentElement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FlowerShopsStockTable {
	/**
	 * @param flowerShopId
	 * @return This function returns the flower stock of a specified flower shop
	 * @throws SQLException
	 */
	public static Map<FlowerType, Integer> getFlowerShopStock(int flowerShopId) throws SQLException {
		Map<FlowerType, Integer> stock = new HashMap<>();
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("""
				SELECT flowershopsstock.id, flowershopsstock.flowertypeid, flowertypes.name AS flowertypename, wholesaleprice, retailprice, flowershopsstock.quantity
				FROM flowershopsstock
				INNER JOIN flowertypes ON flowertypes.id = flowershopsstock.flowertypeid
				INNER JOIN flowershops ON flowershops.id = flowershopsstock.flowershopid
				WHERE flowershops.id = ?
				""")) {
			preparedStatement.setInt(1, flowerShopId);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				FlowerType flowerType = new FlowerType(
						rs.getInt("flowertypeid"),
						rs.getString("flowertypename"),
						rs.getInt("wholesaleprice"),
						rs.getInt("retailprice")
				);
				int quantity = rs.getInt("quantity");
				stock.put(flowerType, quantity);
			}
		}
		return stock;
	}

	/**
	 * This function enables adding flowers to a stock of a specified flower shop
	 *
	 * @param flowerShopId
	 * @param flowerTypeId
	 * @param quantity
	 * @throws SQLException
	 */
	public static void addToFlowerShopStock(int flowerShopId, int flowerTypeId, int quantity) throws SQLException {
		try (Connection conn = DatabaseConnector.connect()) {
			PreparedStatement updateStatement = conn.prepareStatement("""
					UPDATE flowershopsstock SET quantity = quantity + ? WHERE flowershopid = ? AND flowertypeid = ?
					""");
			updateStatement.setInt(1, quantity);
			updateStatement.setInt(2, flowerShopId);
			updateStatement.setInt(3, flowerTypeId);
			if (updateStatement.executeUpdate() <= 0) {
				PreparedStatement insertStatement = conn.prepareStatement("""
						INSERT INTO flowershopsstock(flowershopid, flowertypeid, quantity) VALUES(?, ?, ?)
						""");
				insertStatement.setInt(1, flowerShopId);
				insertStatement.setInt(2, flowerTypeId);
				insertStatement.setInt(3, quantity);
				insertStatement.executeUpdate();
				if (insertStatement.executeUpdate() <= 0) {
					throw new UnknownErrorException();
				}
			}
		}
	}

	/**
	 * This function update flower shop stock after an order is paid.
	 *
	 * @param order the order to be updated
	 * @throws SQLException
	 */
	public static void updateFlowerShopStockAfterOrderPaid(Order order) throws SQLException {
		try (Connection conn = DatabaseConnector.connect()) {
			for (OrderContentElement orderContentElement : OrdersContentTable.getOrderContent(order.getOrderId())) {
				PreparedStatement updateStatement = conn.prepareStatement("""
						UPDATE flowershopsstock SET quantity = quantity - ? WHERE flowershopid = ? AND flowertypeid = ?
						""");
				FlowerType flowerType = (FlowerType) orderContentElement.getStoreItem();
				updateStatement.setInt(1, orderContentElement.getQuantity());
				updateStatement.setInt(2, order.getFlowerShopId());
				updateStatement.setInt(3, flowerType.getId());
				if (updateStatement.executeUpdate() <= 0) {
					throw new UnknownErrorException();
				}
			}
		}
	}

}