package integration.migration;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseMigration {
	
	public static void main(String[] args) throws SQLException {
		DatabaseMigration dbMig = new DatabaseMigration();
	}
	
	private DatabaseMigration() throws SQLException {
		try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:mig_db;create=true")) {
			System.out.println("Source database created!");
		}
	}
	
	private static String generateString(int n) {
		SecureRandom rnd = new SecureRandom();
		char[] genStr = new char[n];
		
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		for (int i = 0; i < n; i++) {
			genStr[i] = alphabet.charAt(rnd.nextInt(alphabet.length()));
		}
		
		return new String(genStr);
	}
}
