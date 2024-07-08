package com.aproteam.flowerpost.exception;

/**
 * An exception in case the order with a specific ID that you are looking for does not exist
 */
public class OrderNotFoundException extends Exception {

	public OrderNotFoundException() {
		super("An order with this id does not exist.");
	}

}
