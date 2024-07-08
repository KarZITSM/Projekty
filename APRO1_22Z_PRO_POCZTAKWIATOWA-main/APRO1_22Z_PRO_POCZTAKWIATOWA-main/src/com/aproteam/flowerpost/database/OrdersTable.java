package com.aproteam.flowerpost.database;

import com.aproteam.flowerpost.exception.OrderNotFoundException;
import com.aproteam.flowerpost.exception.UnknownErrorException;
import com.aproteam.flowerpost.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdersTable {

	/**
	 * This function creates new order
	 *
	 * @param user
	 * @param flowerShop
	 * @return
	 * @throws SQLException
	 */
	public static Order createNewOrder(User user, FlowerShop flowerShop) throws SQLException {
		try (Connection conn = DatabaseConnector.connect()) {
			PreparedStatement statement = conn.prepareStatement("""
					INSERT INTO orders(useremail, flowershopid, orderdate, orderstatus) VALUES(?, ?, ?, ?)
					""");
			Date nowDate = new Date();
			statement.setString(1, user.getEmail());
			statement.setInt(2, flowerShop.getId());
			statement.setDate(3, new java.sql.Date(nowDate.getTime()));
			statement.setString(4, OrderStatus.CREATED.toString());
			if (statement.executeUpdate() <= 0) {
				throw new UnknownErrorException();
			}
			ResultSet rs = statement.getGeneratedKeys();
			if (!rs.next()) {
				throw new UnknownErrorException();
			}
			int orderId = rs.getInt(1);
			return new Order(orderId, nowDate, user.getEmail(), flowerShop.getId(), 0, 0, OrderStatus.CREATED);
		}
	}

	/**
	 * This function updates order's status
	 *
	 * @param userEmail
	 * @param orderId
	 * @param orderStatus
	 * @return
	 * @throws SQLException
	 * @throws OrderNotFoundException
	 */
	public static void updateOrderStatus(String userEmail, int orderId, OrderStatus orderStatus) throws SQLException, OrderNotFoundException {
		try (Connection conn = DatabaseConnector.connect()) {
			PreparedStatement statement = conn.prepareStatement("""
					UPDATE orders SET orderstatus = ?
					WHERE orders.orderid = ? AND useremail = ?
					""");
			statement.setString(1, orderStatus.toString());
			statement.setInt(2, orderId);
			statement.setString(3, userEmail);
			if (statement.executeUpdate() <= 0)
				throw new OrderNotFoundException();
		}
	}

	/**
	 * This function finds orders by a specifc ID
	 *
	 * @param user
	 * @param orderId
	 * @return
	 * @throws SQLException
	 * @throws OrderNotFoundException
	 */
	public static Order findOrderById(User user, int orderId) throws SQLException, OrderNotFoundException {
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("""
				SELECT orders.orderid, useremail, flowershopid, orderdate, orderstatus, COUNT(orderscontent.orderscontentid) AS itemCount, SUM(orderscontent.quantity * retailprice) AS orderPriceSum
				FROM orders
				LEFT JOIN orderscontent ON orderscontent.orderid = orders.orderid
				LEFT JOIN flowertypes ON flowertypes.id = orderscontent.flowertypeid
				WHERE orders.orderid = ? AND (useremail = ? %s) GROUP BY orders.orderid
				""".formatted(user.getPermission() == Permission.ADMIN ? "OR 1 = 1" : ""))) {
			preparedStatement.setInt(1, orderId);
			preparedStatement.setString(2, user.getEmail());
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next())
				throw new OrderNotFoundException();
			return new Order(
					rs.getInt("orderid"),
					new Date(rs.getDate("orderdate").getTime()),
					rs.getString("useremail"),
					rs.getInt("flowershopid"),
					rs.getInt("itemCount"),
					rs.getInt("orderPriceSum"),
					OrderStatus.valueOf(rs.getString("orderstatus")));
		}
	}

	/**
	 * This function returns all the existing orders
	 *
	 * @return
	 * @throws SQLException
	 */
	public static List<Order> getAllOrders() throws SQLException {
		List<Order> orders = new ArrayList<>();
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("""
				SELECT orders.orderid, useremail, flowershopid, orderdate, orderstatus, COUNT(orderscontent.orderscontentid) AS itemCount, SUM(orderscontent.quantity * retailprice) AS orderPriceSum
				FROM orders
				LEFT JOIN orderscontent ON orderscontent.orderid = orders.orderid
				LEFT JOIN flowertypes ON flowertypes.id = orderscontent.flowertypeid
				GROUP BY orders.orderid
				""")) {
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next())
				orders.add(new Order(
						rs.getInt("orderid"),
						new Date(rs.getDate("orderdate").getTime()),
						rs.getString("useremail"),
						rs.getInt("flowershopid"),
						rs.getInt("itemCount"),
						rs.getInt("orderPriceSum"),
						OrderStatus.valueOf(rs.getString("orderstatus"))));
		}
		return orders;
	}

	/**
	 * This function finds orders for users
	 *
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public static List<Order> findOrdersByForUser(User user) throws SQLException {
		List<Order> orders = new ArrayList<>();
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("""
				SELECT orders.orderid, useremail, flowershopid, orderdate, orderstatus, COUNT(orderscontent.orderscontentid) AS itemCount, SUM(orderscontent.quantity * retailprice) AS orderPriceSum
				FROM orders
				LEFT JOIN orderscontent ON orderscontent.orderid = orders.orderid
				LEFT JOIN flowertypes ON flowertypes.id = orderscontent.flowertypeid
				WHERE useremail = ? GROUP BY orders.orderid
				""")) {
			preparedStatement.setString(1, user.getEmail());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next())
				orders.add(new Order(
						rs.getInt("orderid"),
						new Date(rs.getDate("orderdate").getTime()),
						rs.getString("useremail"),
						rs.getInt("flowershopid"),
						rs.getInt("itemCount"),
						rs.getInt("orderPriceSum"),
						OrderStatus.valueOf(rs.getString("orderstatus"))));
		}
		return orders;
	}

	/**
	 * This function finds orders for flower shops
	 *
	 * @param flowerShop
	 * @return
	 * @throws SQLException
	 */
	public static List<Order> findOrdersByForFlowerShop(FlowerShop flowerShop) throws SQLException {
		List<Order> orders = new ArrayList<>();
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("""
				SELECT orders.orderid, useremail, flowershopid, orderdate, orderstatus, COUNT(orderscontent.orderscontentid) AS itemCount, SUM(orderscontent.quantity * retailprice) AS orderPriceSum
				FROM orders
				LEFT JOIN orderscontent ON orderscontent.orderid = orders.orderid
				LEFT JOIN flowertypes ON flowertypes.id = orderscontent.flowertypeid
				WHERE flowershopid = ? GROUP BY orders.orderid
				""")) {
			preparedStatement.setInt(1, flowerShop.getId());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next())
				orders.add(new Order(
						rs.getInt("orderid"),
						new Date(rs.getDate("orderdate").getTime()),
						rs.getString("useremail"),
						rs.getInt("flowershopid"),
						rs.getInt("itemCount"),
						rs.getInt("orderPriceSum"),
						OrderStatus.valueOf(rs.getString("orderstatus"))));
		}
		return orders;
	}

}