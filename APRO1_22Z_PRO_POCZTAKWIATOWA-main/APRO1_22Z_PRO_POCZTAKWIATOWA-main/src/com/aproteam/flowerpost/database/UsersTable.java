package com.aproteam.flowerpost.database;

import com.aproteam.flowerpost.Utils;
import com.aproteam.flowerpost.exception.UnknownErrorException;
import com.aproteam.flowerpost.exception.UserAlreadyExistsException;
import com.aproteam.flowerpost.exception.UserNotFoundException;
import com.aproteam.flowerpost.model.Permission;
import com.aproteam.flowerpost.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersTable {
	/**
	 * A function that allows you to create new user account
	 *
	 * @param name
	 * @param surname
	 * @param email
	 * @param password
	 * @param permission
	 * @return
	 * @throws SQLException
	 * @throws UserAlreadyExistsException
	 */
	public static User createNewUser(String name, String surname, String email, String password, Permission permission) throws SQLException, UserAlreadyExistsException {
		try {
			findUserByEmail(email);
			throw new UserAlreadyExistsException();
		} catch (UserNotFoundException ignored) {
		}
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement statement = conn.prepareStatement("""
				INSERT INTO users(email, name, surname, passwordHash, permission) VALUES(?, ?, ?, ?, ?)
				""")) {
			statement.setString(1, email);
			statement.setString(2, name);
			statement.setString(3, surname);
			statement.setString(4, Utils.hashPassword(password));
			statement.setString(5, permission.name());
			if (statement.executeUpdate() <= 0) {
				throw new UnknownErrorException();
			}
			return new User(name, surname, email, Permission.USER);
		}
	}

	/**
	 * A function that allows users to log in into their accounts
	 *
	 * @param email
	 * @param password
	 * @return
	 * @throws SQLException
	 * @throws UserNotFoundException
	 */
	public static User loginUser(String email, String password) throws SQLException, UserNotFoundException {
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("""
				SELECT * FROM users WHERE email = ? AND passwordHash = ?
				""")) {
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, Utils.hashPassword(password));
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next())
				throw new UserNotFoundException();
			return new User(
					rs.getString("name"),
					rs.getString("surname"),
					rs.getString("email"),
					Permission.valueOf(rs.getString("permission"))
			);
		}
	}

	/**
	 * A function that allows to find users by their e-mail
	 *
	 * @param email
	 * @return
	 * @throws SQLException
	 * @throws UserNotFoundException
	 */
	public static User findUserByEmail(String email) throws SQLException, UserNotFoundException {
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("""
				SELECT * FROM users WHERE email = ?
				""")) {
			preparedStatement.setString(1, email);
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next())
				throw new UserNotFoundException();
			return new User(
					rs.getString("name"),
					rs.getString("surname"),
					rs.getString("email"),
					Permission.valueOf(rs.getString("permission"))
			);
		}
	}

	/**
	 * This function returns the amount of user accounts that exist
	 *
	 * @return
	 * @throws SQLException
	 */
	public static int getAllUsersCount() throws SQLException {
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("SELECT Count(*) FROM users")) {
			ResultSet rs = preparedStatement.executeQuery();
			if (!rs.next()) {
				throw new UnknownErrorException();
			}
			return rs.getInt(1);
		}
	}

	/**
	 * This function returns all information about all existing accounts
	 *
	 * @return
	 * @throws SQLException
	 */
	public static List<User> getAllUsers() throws SQLException {
		List<User> users = new ArrayList<>();
		try (Connection conn = DatabaseConnector.connect(); PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM users")) {
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next())
				users.add(new User(
						rs.getString("name"),
						rs.getString("surname"),
						rs.getString("email"),
						Permission.valueOf(rs.getString("permission"))
				));
		}
		return users;
	}

}