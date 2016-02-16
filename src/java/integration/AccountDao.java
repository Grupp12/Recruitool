package integration;

import controller.ValidationException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import model.Account;

/**
 * DAO for handling Accounts in the database using JPA
 */
@Stateless
public class AccountDao {

	@PersistenceContext(unitName = "RecruitoolPU")
	EntityManager em;

	/**
	 * Persist account to database through JPA
	 *
	 * @param acc the account to persist.
	 * 
	 * @throws controller.ValidationException
	 * @throws EntityExistsException if account already exists.
	 */
	public void persistAccount(Account acc) throws ValidationException, EntityExistsException {
		try {
			Account found = em.find(Account.class, acc.getUsername());
			if (found != null) {
				throw new EntityExistsException("Account with username '" + acc.getUsername() + "' already exists.");
			}
			em.persist(acc);
		} catch (ConstraintViolationException ex) {
			StringBuilder violationsStr = new StringBuilder();
			for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
				violationsStr.append(violation.getMessage()).append('\n');
			}
			
			throw new ValidationException(violationsStr.toString());
		}
	}
}
