package com.aproteam.flowerpost.database;

import com.aproteam.flowerpost.exception.UnknownErrorException;
import com.aproteam.flowerpost.model.FlowerType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FlowerTypesTable {
	/**
	 * This function enables creation of a new flower type
	 *
	 * @param name
	 * @param wholesalePrice
	 * @param retailPrice
	 * @return
	 * @throws SQLException
	 */
	public static FlowerType createNewFlowerType(String name, int wholesalePrice, int retailPrice) throws SQLException {
		try (Connection conn = DatabaseConnector.connect()) {
			PreparedStatement statement = conn.prepareStatement("INSERT INTO flowertypes(name, wholesalePrice, retailPrice) VALUES(?, ?, ?)");
			statement.setString(1, name);
			statement.setInt(2, wholesalePrice);
			statement.setInt(3, retailPrice);
			if (statement.executeUpdate() <= 0) {
				throw new UnknownErrorException();
			}
			ResultSet rs = statement.getGeneratedKeys();
			if (!rs.next()) {
				throw new UnknownErrorException();
			}
			int flowerTypeId = rs.getInt(1);
			return new FlowerType(flowerTypeId, name, wholesalePrice, retailPrice);
		}
	}

	public static List<FlowerType> getAllFlowerTypes() throws SQLException {
		List<FlowerType> flowerTypes = new ArrayList<>();
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM flowertypes")) {
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				FlowerType flowerType = new FlowerType(
						rs.getInt("id"),
						rs.getString("name"),
						rs.getInt("wholesalePrice"),
						rs.getInt("retailPrice")
				);
				flowerTypes.add(flowerType);
			}
		}
		return flowerTypes;
	}

}