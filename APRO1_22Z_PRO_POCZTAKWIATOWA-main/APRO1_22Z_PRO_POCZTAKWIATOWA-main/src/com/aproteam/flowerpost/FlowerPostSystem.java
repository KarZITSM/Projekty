package com.aproteam.flowerpost;

import com.aproteam.flowerpost.database.DatabaseConnector;
import com.aproteam.flowerpost.model.FlowerShop;
import com.aproteam.flowerpost.model.User;
import com.aproteam.flowerpost.ui.UserInterfaceOfFlowerShopSystem;

import java.util.List;

/**
 * A class that initiates needed variables
 */
public class FlowerPostSystem {

	/**
	 * Interface of the flower post system
	 */
	private UserInterfaceOfFlowerShopSystem userInterfaceOfFlowerShopSystem;

	/**
	 * The status of users account
	 */
	private User loggedInUser;

	/**
	 * The list of flower shops that function in this system
	 */
	private List<FlowerShop> flowerShops;

	public static void main(String[] args) {
		FlowerPostSystem flowerPostSystem = new FlowerPostSystem();
		flowerPostSystem.startUI();
	}

	/**
	 * A constructor in FlowerPostSystem class
	 */
	public FlowerPostSystem() {
		DatabaseConnector.createTablesIfNeeded();
	}

	public void startUI() {
		userInterfaceOfFlowerShopSystem = new UserInterfaceOfFlowerShopSystem(this);
		userInterfaceOfFlowerShopSystem.mainMenu();
	}

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public List<FlowerShop> getFlowerShops() {
		return flowerShops;
	}

}