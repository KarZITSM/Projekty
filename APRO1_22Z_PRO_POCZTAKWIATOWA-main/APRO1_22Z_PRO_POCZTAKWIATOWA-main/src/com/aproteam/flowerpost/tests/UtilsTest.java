import com.aproteam.flowerpost.Utils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class UtilsTest {
	/**
	 * A test that checks if the hashing of a password works correctly
	 */
	@Test
	public void testHashPassword() {
		String password = "mypassword";
		String hashed = Utils.hashPassword(password);
		assertNotEquals(password, hashed);
		assertNotNull(hashed);
		assertTrue(hashed.matches("^[a-fA-F0-9]+$")); // check that the hashed password is a valid hex string
		assertEquals(64, hashed.length()); // check that the hashed password is 64 characters long (for SHA-256)
	}

	/**
	 * A test that checks if the function BytesToHex works correctly
	 */
	@Test
	public void testBytesToHex() {
		byte[] bytes = new byte[]{(byte) 0xAB, (byte) 0xCD, (byte) 0xEF};
		String hex = Utils.bytesToHex(bytes);
		assertEquals("ABCDEF", hex);
	}

	/**
	 * A test that checks if the working directory exists
	 */
	@Test
	public void testGetWorkingDirectory() {
		File workingDir = Utils.getWorkingDirectory();
		assertTrue(workingDir.exists());
	}

	/**
	 * A test that checks if the format of the data is formulated correctly
	 */
	@Test
	public void testFormatDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm.ss");
		try {
			Date date = formatter.parse("2023.01.22 14:19.10");
			String expectedOutput = "2023.01.22 14:19.10";
			String formatted = Utils.formatDate(date);
			assertEquals(expectedOutput, formatted);
		} catch (ParseException e) {
			fail("Failed to parse date string: " + e.getMessage());
		}
	}
}