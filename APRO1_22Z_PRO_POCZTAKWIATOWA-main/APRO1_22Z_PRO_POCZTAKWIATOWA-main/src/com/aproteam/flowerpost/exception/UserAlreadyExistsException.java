package com.aproteam.flowerpost.exception;

/**
 * An exception in case a user wants to create account with and e-mail that has already been used for that purpose
 */
public class UserAlreadyExistsException extends Exception {

	public UserAlreadyExistsException() {
		super("User with this email already exists.");
	}

}
