package integration.migration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseMigrator {
	
	public static void main(String[] args) throws SQLException, IOException {
		DatabaseMigrator dbMig = new DatabaseMigrator();
	}
	
	private final Connection conn;
	
	private DatabaseMigrator() throws SQLException, IOException {
		conn = DriverManager.getConnection("jdbc:derby:memory:mig_db;create=true");
		System.out.println("Source database created!");
		
		parseOldSqlScript();
		
		ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM person");
		while (rs.next()) {
			System.out.println(rs.getString("name"));
		}
		
		conn.close();
	}
	
	private void parseOldSqlScript() throws IOException {
		InputStream inStream = new FileInputStream("old.sql");
		Reader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
		
		StringBuilder statement = new StringBuilder();
		
		int in;
		while ((in = reader.read()) != -1) {
			char ch = (char)in;
			if (ch == ';') {
				try {
					conn.createStatement().execute(statement.toString());
				} catch (SQLException ex) {
					Logger.getLogger(DatabaseMigrator.class.getName()).log(Level.SEVERE, null, ex);
				} finally {
					statement = new StringBuilder();
				}
			}
			else {
				statement.append(ch);
			}
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
