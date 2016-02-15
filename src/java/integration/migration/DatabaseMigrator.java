package integration.migration;

import integration.AccountDao;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import model.Account;
import security.Crypto;

public class DatabaseMigrator {
	
	public static void main(String[] args) throws SQLException, IOException, NamingException {
		DatabaseMigrator dbMig = new DatabaseMigrator();
	}
	
	private static final Logger logger = Logger.getLogger(DatabaseMigrator.class.getName());
	
	private final Connection legacyConn;
	private EntityManager em;
	
	private HashMap<Integer, String> roles;
	
	protected DatabaseMigrator() throws SQLException, IOException, NamingException {
		legacyConn = DriverManager.getConnection("jdbc:derby:memory:mig_db;create=true");
		System.out.println("Source database created!");
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("recruitool");
		em = emfactory.createEntityManager();
		
		logger.log(Level.INFO, "Connection established");
		
		parseLegacySqlScript();
		
		loadRoles();
		
		migrateAccounts();

		//em.close();
		//emfactory.close();
		
		legacyConn.close();
	}
	
	private void loadRoles() throws SQLException {
		roles = new HashMap<>();
		
		Statement stmt = legacyConn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM role");
		while (rs.next()) {
			int id = rs.getInt("role_id");
			String name = rs.getString("name").toUpperCase();
			
			// Fix wrong name in legacy database
			if (name.equals("RECRUIT"))
				name = "RECRUITER";
			
			roles.put(id, name);
		}
		
		stmt.close();
	}
	
	private void migrateAccounts() throws SQLException {
		//em.getTransaction().begin();
		
		Statement stmt = legacyConn.createStatement();
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM person");
		while (rs.next()) {
			String firstName = rs.getString("name");
			String lastName = rs.getString("surname");
			String ssn = rs.getString("ssn");
			String email = rs.getString("email");
			String username = rs.getString("username");
			String password = rs.getString("password");
			
			int roleId = rs.getInt("role_id");
			String role = roles.get(roleId);
			
			if (role.equals("APPLICANT")) {
				// Applicants does not have a password or username
				// in the legacy database, so we must generate them.
				
				username = generateString(6);
				password = generateString(6);
			}
			
			// Hash the password
			password = Crypto.generateHash(password);
			
			Account account = new Account(
					firstName, lastName, ssn,
					email, username, password, role
			);
			
			try {
				//em.persist(account);
			} catch (Exception ex) {
				logger.log(Level.SEVERE, null, ex);
			}
		}
		
		stmt.close();
		
		//em.getTransaction().commit();
	}
	
	private void parseLegacySqlScript() throws IOException {
		InputStream inStream = new FileInputStream("old.sql");
		Reader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
		
		StringBuilder statement = new StringBuilder();
		
		int in;
		while ((in = reader.read()) != -1) {
			char ch = (char)in;
			if (ch == ';') {
				try {
					legacyConn.createStatement().execute(statement.toString());
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
