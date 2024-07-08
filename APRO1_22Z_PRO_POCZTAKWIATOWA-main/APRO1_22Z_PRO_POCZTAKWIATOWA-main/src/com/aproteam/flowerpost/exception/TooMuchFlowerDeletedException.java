package com.aproteam.flowerpost.exception;

/**
 * An exception in case that a user wants to delete too many items from their order
 */
public class TooMuchFlowerDeletedException extends Exception {
	public TooMuchFlowerDeletedException() {
		super("Quantity requested to delete is more than the quantity in the order");
	}
}
