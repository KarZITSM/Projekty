package com.aproteam.flowerpost.exception;

/**
 * An exception in case the flower shop does not have the item that you are looking for in stock/ or has to few of them
 */
public class StoreItemNotAvailableException extends Exception {

	public StoreItemNotAvailableException() {
		super("The item with specified quantity is not available in this flower shop.");
	}

}
