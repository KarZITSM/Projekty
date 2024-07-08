package com.aproteam.flowerpost;

import java.io.Console;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents hashing passwords of our users in order to keep them safe and private
 */
public class Utils {
	/**
	 * Correct format of the date
	 */
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm.ss");
	/**
	 * Hex array
	 */
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

	/**
	 * Hashes password using SHA-256 algorithm
	 * @param password the password to be hashed
	 * @return hashed password or null if there was unresolved error
	 */
	public static String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(hash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Converts bytes into theirs hex string representation
	 *
	 * @param bytes data to be converted
	 * @return Uppercase string that is a hex representation of the bytes array.
	 */
	public static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * @return the path to program's working directory.
	 */
	public static File getWorkingDirectory() {
		return new File(System.getProperty("user.dir"));
	}

	/**
	 * Tries to clear output console, works in Windows Command Line and Linux Terminal
	 */
	public static void clearOutputConsole() {
		try {
			String os = System.getProperty("os.name");
			if (os.contains("Windows")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			} else {
				Runtime.getRuntime().exec("clear");
			}
		} catch (Exception ignored) {
		}
	}

	/**
	 * Promts user for input password and hides it if the console support this feature.
	 * @return password that was inputted by the user
	 */
	public static String promptForPasswordInput() {
		Console console = System.console();
		if (console == null) {
			return Constants.scanner.nextLine();
		}
		char[] passwordArray = console.readPassword();
		return new String(passwordArray);
	}

	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

}