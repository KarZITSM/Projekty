package com.aproteam.flowerpost.database;

import com.aproteam.flowerpost.Utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

	public static String DATABASE_FILENAME = "flowerpost.db";

	private static File getLocalDatabaseFile() {
		return new File(Utils.getWorkingDirectory(), DATABASE_FILENAME);
	}

	/**
	 * Establishes connection to MySQLite database.
	 */
	public static Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + getLocalDatabaseFile());
	}

	/**
	 * Creates tables in the database if it is needed
	 */
	public static void createTablesIfNeeded() {
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			stmt.execute("CREATE TABLE IF NOT EXISTS users (email text(64) NOT NULL PRIMARY KEY, name text NOT NULL, surname text NOT NULL, passwordHash text NOT NULL, permission text NOT NULL);");
			stmt.execute("CREATE TABLE IF NOT EXISTS flowershops (id integer PRIMARY KEY AUTOINCREMENT, name text NOT NULL, address text NOT NULL);");
			stmt.execute("CREATE TABLE IF NOT EXISTS flowertypes (id integer PRIMARY KEY AUTOINCREMENT, name text NOT NULL, wholesalePrice integer, retailprice integer);");
			stmt.execute("CREATE TABLE IF NOT EXISTS flowershopsstock (id integer PRIMARY KEY AUTOINCREMENT, flowershopid integer, flowertypeid integer, quantity integer);");
			stmt.execute("CREATE TABLE IF NOT EXISTS orders (orderid integer PRIMARY KEY AUTOINCREMENT, useremail text NOT NULL, flowershopid integer, orderdate datetime, orderstatus text NOT NULL);");
			stmt.execute("CREATE TABLE IF NOT EXISTS orderscontent (orderscontentid integer PRIMARY KEY AUTOINCREMENT, orderid integer, flowertypeid integer, quantity integer);");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Drop all tables in the database
	 */
	public static void dropAllTables() {
		try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
			stmt.execute("DROP TABLE IF EXISTS users;");
			stmt.execute("DROP TABLE IF EXISTS flowershops;");
			stmt.execute("DROP TABLE IF EXISTS flowertypes;");
			stmt.execute("DROP TABLE IF EXISTS flowershopsstock;");
			stmt.execute("DROP TABLE IF EXISTS orders;");
			stmt.execute("DROP TABLE IF EXISTS orderscontent;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}