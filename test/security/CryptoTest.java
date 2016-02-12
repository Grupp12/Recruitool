package security;

import org.junit.Test;
import static org.junit.Assert.*;

public class CryptoTest {

	/**
	 * Test the generation and validation of hashes in the {@code Crypto} class.
	 */
	@Test
	public void testGenerateHash() {
		System.out.println("generateHash");
		String clear = "password";
		String hash = Crypto.generateHash(clear);
		assertTrue(Crypto.validateHash(clear, hash));
		assertFalse(Crypto.validateHash("drowssap", hash));
		assertFalse(Crypto.validateHash("", hash));
	}
}
