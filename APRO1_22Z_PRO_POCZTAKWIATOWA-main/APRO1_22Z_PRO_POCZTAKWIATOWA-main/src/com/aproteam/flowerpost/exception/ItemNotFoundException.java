package com.aproteam.flowerpost.exception;

/**
 * An exception in case the item that you are looking for in your order does not exist
 */
public class ItemNotFoundException extends Exception {
	public ItemNotFoundException() {
		super("There is no such flower in your order");
	}


}
