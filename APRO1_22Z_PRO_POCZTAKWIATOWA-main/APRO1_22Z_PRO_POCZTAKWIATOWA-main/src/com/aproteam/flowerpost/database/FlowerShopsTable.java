package com.aproteam.flowerpost.database;

import com.aproteam.flowerpost.exception.FlowerShopNotFoundException;
import com.aproteam.flowerpost.exception.UnknownErrorException;
import com.aproteam.flowerpost.model.FlowerShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlowerShopsTable {
	/**
	 * This function enables the creation of new flower shops
	 *
	 * @param name
	 * @param address
	 * @return
	 * @throws SQLException
	 */
	public static FlowerShop createNewFlowerShop(String name, String address) throws SQLException {
		try (Connection conn = DatabaseConnector.connect()) {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO flowershops(name, address) VALUES(?, ?)");
			statement.setString(1, name);
			statement.setString(2, address);
			if (statement.executeUpdate() <= 0) {
				throw new UnknownErrorException();
			}
			ResultSet rs = statement.getGeneratedKeys();
			if (!rs.next()) {
				throw new UnknownErrorException();
			}
			int flowerShopId = rs.getInt(1);
			return new FlowerShop(flowerShopId, name, address);
		}
	}

	public static List<FlowerShop> getAllFlowerShops() throws SQLException {
		List<FlowerShop> flowerShops = new ArrayList<>();
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("""
				SELECT * FROM flowershops
				""")) {
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				FlowerShop flowerShop = new FlowerShop(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getString("address")
				);
				flowerShops.add(flowerShop);
			}
		}
		return flowerShops;
	}

	/**
	 * This function allows to find specific flower shop by their ID
	 *
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws FlowerShopNotFoundException
	 */
	public static FlowerShop findFlowerShopById(int id) throws SQLException, FlowerShopNotFoundException {
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM flowershops WHERE id = ?")) {
			preparedStatement.setInt(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next())
				throw new FlowerShopNotFoundException();
			return new FlowerShop(
					rs.getInt("id"),
					rs.getString("name"),
					rs.getString("address")
			);
		}
	}

}