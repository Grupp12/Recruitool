package integration.migration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.account.Role;

import security.Crypto;

/**
 * The {@code DatabaseMigrator} is a small program that is
 * used for migrating the legacy database into our new database
 */
public class DatabaseMigrator {
	
	private DatabaseMigrator() {
	}
	
	private static final Logger logger = Logger.getLogger(DatabaseMigrator.class.getName());
	
	public static void main(String[] args) {
		migrate();
	}
	
	private static Connection oldConn;
	private static Connection newConn;
	
	private static HashMap<Integer, Role> roles;
	
	private static void migrate() {
		try {
			oldConn = DriverManager.getConnection("jdbc:derby:memory:mig_db;create=true");
			parseLegacySqlScript();
			logger.log(Level.INFO, "Legacy database created!");

			newConn = DriverManager.getConnection("jdbc:derby://localhost:1527/recruitool;user=root;password=1234");
			logger.log(Level.INFO, "Connected to new database!");

			loadRoles();
			logger.log(Level.INFO, "{0} roles loaded.", roles.size());

			int numAccs = migrateAccounts();
			logger.log(Level.INFO, "{0} accounts loaded.", numAccs);

			newConn.close();
			oldConn.close();

			logger.log(Level.INFO, "Database migration completed!");
		}
		catch (SQLException | IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}
	}
	
	private static void loadRoles() throws SQLException {
		roles = new HashMap<>();
		
		Statement stmt = oldConn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM role");
		while (rs.next()) {
			int id = rs.getInt("role_id");
			String name = rs.getString("name").toUpperCase();
			
			// Fix wrong name in legacy database
			if (name.equals("RECRUIT"))
				name = "RECRUITER";
			
			roles.put(id, Role.valueOf(name));
		}
		
		stmt.close();
	}
	
	private static int migrateAccounts() throws SQLException {
		String newAccSql = "INSERT INTO account " +
				"(firstname, lastname, ssn, email, username, password, acc_role) " +
				"VALUES(?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement newAccStmt = newConn.prepareStatement(newAccSql);
		
		Statement stmt = oldConn.createStatement();
		
		int numAccs = 0;
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM person");
		while (rs.next()) {
			String firstName = rs.getString("name");
			String lastName = rs.getString("surname");
			//String ssn = rs.getString("ssn");
			String email = rs.getString("email");
			String username = rs.getString("username");
			String password = rs.getString("password");
			
			int roleId = rs.getInt("role_id");
			Role role = roles.get(roleId);
			
			if (role == Role.APPLICANT) {
				// Applicants does not have a password or username
				// in the legacy database, so we must generate them.
				
				username = generateString(6);
				password = generateString(6);
			}
			
			// Hash the password
			password = Crypto.generateHash(password);
			
			// Set statement variables
			newAccStmt.setString(1, firstName);
			newAccStmt.setString(2, lastName);
			//newAccStmt.setString(3, ssn);
			newAccStmt.setString(4, email);
			newAccStmt.setString(5, username);
			newAccStmt.setString(6, password);
			newAccStmt.setString(7, role.toString());
			
			newAccStmt.executeUpdate();
			numAccs++;
		}
		
		newAccStmt.close();
		stmt.close();
		
		return numAccs;
	}
	
	private static void parseLegacySqlScript() throws IOException {
		InputStream inStream = new FileInputStream("old.sql");
		Reader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
		
		StringBuilder statement = new StringBuilder();
		
		int in;
		while ((in = reader.read()) != -1) {
			char ch = (char)in;
			if (ch == ';') {
				try {
					Statement stmt = oldConn.createStatement();
					stmt.execute(statement.toString());
					stmt.close();
				} catch (SQLException ex) {
					logger.log(Level.SEVERE, null, ex);
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
