package integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import model.ValidationException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.MANDATORY;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import model.Account;

/**
 * DAO for handling Accounts in the database using JPA
 */
@TransactionAttribute(MANDATORY)
@Stateless
public class AccountDao {

	@PersistenceContext(unitName = "RecruitoolPU")
	private EntityManager em;

	/**
	 * Persist account to database through JPA
	 *
	 * @param acc the account to persist.
	 *
	 * @throws model.ValidationException
	 * @throws EntityExistsException if account already exists.
	 */
	public void persistAccount(Account acc) throws ValidationException, EntityExistsException {
		try {
			Account found = em.find(Account.class, acc.getUsername());
			if (found != null) {
				throw new EntityExistsException("Account with username '" + acc.getUsername() + "' already exists.");
			}
			em.persist(acc);
			AccountGroups ag = new AccountGroups();
			ag.username = acc.getUsername();
			em.persist(ag);
		} catch (ConstraintViolationException ex) {
			StringBuilder violationsStr = new StringBuilder();
			for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
				violationsStr.append(violation.getMessage()).append('\n');
			}

			throw new ValidationException(violationsStr.toString());
		}
	}

	/**
	 * Retrieves the account object with the specified username
	 * @param username
	 * @return 
	 */
	public Account getAccount(String username) {
		return em.find(Account.class, username);
	}
	
	/**
	 * Internal class for easy JPA adding of account-to-group relations in database
	 */
	@Entity
	@Table(name="ACCOUNT_GROUPS")
	private static class AccountGroups {
		@Id
		@Column(name="GROUPNAME")
		public final String groupName = "APPLICANT";
		@Id
		@Column(name="USERNAME")
		public String username;
	}
	
}
