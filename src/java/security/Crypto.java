package security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Reference: http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
 */
public class Crypto {
	
	private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final int ITERATIONS = 1000;
	private static final int KEY_LENGTH = 1024;
	private static final int SALT_LENGTH = 64;
	
	public static String generateHash(String clear) {
		try {
			SecureRandom srnd = SecureRandom.getInstance("SHA1PRNG", "SUN");
			byte[] salt = new byte[SALT_LENGTH];
			srnd.nextBytes(salt);
		
			PBEKeySpec keySpec = new PBEKeySpec(clear.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
			SecretKeyFactory keyFact = SecretKeyFactory.getInstance(ALGORITHM);
			
			byte[] hash = keyFact.generateSecret(keySpec).getEncoded();
			
			String saltStr = Base64.getEncoder().encodeToString(salt);
			String hashStr = Base64.getEncoder().encodeToString(hash);
			return saltStr + ":" + hashStr;
		} catch (Exception ex) {
			Logger.getLogger(Crypto.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException("Ka-Boom!");
		}
	}
	
	public static boolean validateHash(String clear, String hashed) {
		try {
			String[] parts = hashed.split(":");
			
			byte[] salt = Base64.getDecoder().decode(parts[0]);
			byte[] hash = Base64.getDecoder().decode(parts[1]);
			
			PBEKeySpec keySpec = new PBEKeySpec(clear.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
			SecretKeyFactory keyFact = SecretKeyFactory.getInstance(ALGORITHM);
			byte[] testHash = keyFact.generateSecret(keySpec).getEncoded();
			
			/*int diff = hash.length ^ testHash.length;
			for (int i = 0; i < hash.length && i < testHash.length; i++) {
				diff |= hash[i] ^ testHash[i];
			}
			return diff == 0;*/
			if (hash.length != testHash.length)
				return false;
			for (int i = 0; i < hash.length; i++) {
				if (hash[i] != testHash[i])
					return false;
			}
			return true;
		} catch (Exception ex) {
			Logger.getLogger(Crypto.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException("Ka-Boom!");
		}
	}
}
