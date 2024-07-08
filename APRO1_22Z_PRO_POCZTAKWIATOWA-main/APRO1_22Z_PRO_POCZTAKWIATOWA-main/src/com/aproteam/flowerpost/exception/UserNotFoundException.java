package com.aproteam.flowerpost.exception;

/**
 * An exception in case a user with a specific e-mail that you are looking for does not exist
 */
public class UserNotFoundException extends Exception {

	public UserNotFoundException() {
		super("User with this email does not exist.");
	}

}
