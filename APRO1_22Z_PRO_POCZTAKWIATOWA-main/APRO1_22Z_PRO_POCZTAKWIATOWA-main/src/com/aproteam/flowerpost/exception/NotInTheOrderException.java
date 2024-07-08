package com.aproteam.flowerpost.exception;

/**
 * An exception in case the item that you are looking for in your order does not exist
 */
public class NotInTheOrderException extends Exception {
	public NotInTheOrderException() {
		super("Item not found in the order");
	}
}
