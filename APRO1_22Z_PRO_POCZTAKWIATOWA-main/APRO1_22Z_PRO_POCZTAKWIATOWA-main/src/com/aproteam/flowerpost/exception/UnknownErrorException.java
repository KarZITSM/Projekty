package com.aproteam.flowerpost.exception;

/**
 * An exception in case of an unknown order
 */
public class UnknownErrorException extends RuntimeException {

	public UnknownErrorException() {
		super("An unknown error occurred.");
	}

}
