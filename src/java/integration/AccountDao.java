package integration;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Account;

@Stateless
public class AccountDao {

	@PersistenceContext(unitName = "RecruitoolPU")
	EntityManager em;

	/**
	 * Persist account to database.
	 *
	 * @param acc the account to persist.
	 * @throws EntityExistsException if account already exists.
	 */
	public void persistAccount(Account acc) throws EntityExistsException {
		Account found = em.find(Account.class, acc.getUsername());
		if (found != null) {
			throw new EntityExistsException("Account with username '" + acc.getUsername() + "' already exists.");
		}
		em.persist(acc);
	}
}
