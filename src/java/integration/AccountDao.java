package integration;

import java.io.Serializable;
import model.ValidationException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.MANDATORY;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
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
			if (getAccount(acc.getUsername()) != null) {
				throw new EntityExistsException("Account with username '" + acc.getUsername() + "' already exists.");
			}
			
			AccountGroups ag = new AccountGroups();
			ag.username = acc.getUsername();
			ag.acc=acc;
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
		TypedQuery<Account> query = em.createQuery("SELECT a FROM Account a " +
					"WHERE UPPER(a.username) = UPPER(:uname)", Account.class);
		query.setParameter("uname", username);
			
		if (query.getResultList().isEmpty()) {
			return null;
		}
		
		return query.getResultList().get(0);
	}
	
	/**
	 * Internal class for easy JPA adding of account-to-group relations in database
	 */
	@Entity
	@Table(name="ACCOUNT_GROUPS")
	private static class AccountGroups implements Serializable {
		
		@JoinColumn(name="ACCOUNT")
		@OneToOne(fetch = FetchType.LAZY, optional = false, 
				targetEntity = Account.class, cascade = {CascadeType.ALL})
		public Account acc;
		
		@Id
		@Column(name="GROUPNAME")
		public final String groupName = "APPLICANT";
		@Id
		@Column(name="USERNAME")
		public String username;
	}
}
