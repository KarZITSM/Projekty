package com.aproteam.flowerpost.exception;

/**
 * An exception in case a flower shop with a specific ID, name or address does not exist
 */
public class FlowerShopNotFoundException extends Exception {

	public FlowerShopNotFoundException() {
		super("Flower Shop does not exist.");
	}

}
