package integration.migration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private static HashMap<Long, String> roles = new HashMap<>();
	private static HashMap<Long, String> competences = new HashMap<>();
	
	// Legacy objects
	private static HashMap<Long, LegacyAccount> accounts = new HashMap<>();
	private static HashMap<Long, LegacyAvailability> availabilities = new HashMap<>();
	private static HashMap<Long, LegacyCompetenceProfile> profiles = new HashMap<>();
	
	private static void migrate() {
		try {
			oldConn = DriverManager.getConnection("jdbc:derby:memory:mig_db;create=true");
			parseLegacySqlScript();
			logger.log(Level.INFO, "Legacy database created!");

			newConn = DriverManager.getConnection("jdbc:derby://localhost:1527/recruitool;user=root;password=1234");
			logger.log(Level.INFO, "Connected to new database!");

			loadLegacyTables();
			oldConn.close();

			migrateAccounts();
			migrateApplications();
			
			newConn.close();

			logger.log(Level.INFO, "Database migration completed!");
		}
		catch (SQLException | IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage());
		}
	}
	
	private static void loadLegacyTables() throws SQLException {
		loadRoles();
		logger.log(Level.INFO, "{0} roles loaded.", roles.size());

		loadAccounts();
		logger.log(Level.INFO, "{0} accounts loaded.", accounts.size());
		
		loadAvailabilities();
		logger.log(Level.INFO, "{0} availabilities loaded.", availabilities.size());
		
		loadCompetences();
		logger.log(Level.INFO, "{0} competences loaded.", competences.size());
		
		loadCompetenceProfiles();
		logger.log(Level.INFO, "{0} competence profiles loaded.", competences.size());
	}
	
	private static void loadRoles() throws SQLException {
		Statement stmt = oldConn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM role");
		while (rs.next())
		{
			long id = rs.getLong("role_id");
			
			String name = rs.getString("name").toUpperCase();
			
			// Fix wrong name in legacy database
			if (name.equals("RECRUIT"))
				name = "RECRUITER";
			
			roles.put(id, name);
		}
		
		stmt.close();
	}
	
	private static void loadAccounts() throws SQLException {
		Statement stmt = oldConn.createStatement();
			
		ResultSet rs = stmt.executeQuery("SELECT * FROM person");
		while (rs.next())
		{
			long id = rs.getLong("person_id");
			
			String firstName = rs.getString("name");
			String lastName = rs.getString("surname");
			
			String ssn = rs.getString("ssn");
			
			String email = rs.getString("email");
			
			String username = rs.getString("username");
			String password = rs.getString("password");
			
			long role_id = rs.getLong("role_id");
			
			if (roles.get(role_id).equals("APPLICANT")) {
				// Applicants does not have a password or username
				// in the legacy database, so we must generate them.
				
				username = generateString(6);
				password = generateString(6);
			}
			
			// Hash the password
			password = Crypto.generateHash(password);
			
			// Create legacy account
			LegacyAccount account = new LegacyAccount();
			
			account.firstName = firstName;
			account.lastName = lastName;
			
			account.ssn = ssn;
			
			account.email = email;
			
			account.username = username;
			account.password = password;
			
			account.role_id = role_id;
			
			accounts.put(id, account);
		}
		
		stmt.close();
	}
	
	private static void loadAvailabilities() throws SQLException {
		Statement stmt = oldConn.createStatement();
			
		ResultSet rs = stmt.executeQuery("SELECT * FROM availability");
		while (rs.next())
		{
			long id = rs.getLong("availability_id");
			
			Date fromDate = rs.getDate("from_date");			
			Date toDate = rs.getDate("to_date");
			
			long account_id = rs.getLong("person_id");
			
			// Create legacy availability
			LegacyAvailability availability = new LegacyAvailability();
			
			availability.id = id;
			
			availability.fromDate = fromDate;
			availability.toDate = toDate;
			
			availability.account_id = account_id;
			
			availabilities.put(id, availability);
		}
		
		stmt.close();
	}
	
	private static void loadCompetences() throws SQLException {
		Statement stmt = oldConn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM competence");
		while (rs.next())
		{
			long id = rs.getLong("competence_id");
			
			String name = rs.getString("name").toUpperCase();
			
			competences.put(id, name);
		}
		
		stmt.close();
	}
	
	private static void loadCompetenceProfiles() throws SQLException {
		Statement stmt = oldConn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM competence_profile");
		while (rs.next())
		{
			long id = rs.getLong("competence_profile_id");
			
			int yearsOfExp = rs.getInt("years_of_experience");
			
			long account_id = rs.getLong("person_id");
			long competence_id = rs.getLong("competence_id");
			
			// Create legacy competence profile
			LegacyCompetenceProfile profile = new LegacyCompetenceProfile();
			
			profile.id = id;
			
			profile.yearsOfExp = yearsOfExp;
			
			profile.account_id = account_id;
			profile.competence_id = competence_id;
			
			profiles.put(id, profile);
		}
		
		stmt.close();
	}
	
	private static void migrateAccounts() throws SQLException {
		String newAccSql = "INSERT INTO account " +
				"(firstname, lastname, email, username, password, acc_role) " +
				"VALUES(?, ?, ?, ?, ?, ?)";
		PreparedStatement newAccStmt = newConn.prepareStatement(newAccSql);
		
		for (LegacyAccount account : accounts.values())
		{
			newAccStmt.setString(1, account.firstName);
			newAccStmt.setString(2, account.lastName);
			
			newAccStmt.setString(3, account.email);
			
			newAccStmt.setString(4, account.username);
			newAccStmt.setString(5, account.password);
			
			String acc_role = roles.get(account.role_id);
			newAccStmt.setString(6, acc_role);
			
			newAccStmt.execute();
		}
		
		newAccStmt.close();
	}
	
	private static void migrateApplications() throws SQLException {
		
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
