package com.aproteam.flowerpost.model;

/**
 * This class represents user-information about our account
 */
public class User {
	/**
	 * @param name Users name
	 */
	private final String name;
	/**
	 * @param surname Users surname
	 */
	private final String surname;
	/**
	 * @param email Users e-mail address
	 */
	private final String email;
	/**
	 * @param permission Users administrator status
	 */
	private final Permission permission;

	/**
	 * A constructor in User class
	 *
	 * @param name       Users name
	 * @param surname    Users surname
	 * @param email      Users e-mail address
	 * @param permission Users administrator status
	 */
	public User(String name, String surname, String email, Permission permission) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.permission = permission;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getEmail() {
		return email;
	}

	public Permission getPermission() {
		return permission;
	}

	public String getFullName() {
		return name + " " + surname;
	}

	public Object[] getFieldValues() {
		return new Object[]{name, surname, email, permission};
	}

	public static String[] getFieldNames() {
		return new String[]{"name", "surname", "email", "permission"};
	}

}