package controller;

import model.ValidationException;
import integration.AccountDao;
import integration.EntityExistsException;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import model.account.Account;
import view.RegisterFormDTO;

/**
 * The {@code Controller} is responsible for all business logic.
 */
@Stateful
public class Controller {

	@EJB
	private AccountDao accountDao;

	/**
	 * Registers a new {@code Account}.
	 *
	 * @param registerForm the new account info.
	 * 
	 * @throws ValidationException if account data is invalid.
	 * @throws EntityExistsException if account already exists.
	 */
	public void register(RegisterFormDTO registerForm) throws ValidationException, EntityExistsException {
		Account acc = new Account(registerForm);
		
		accountDao.persistAccount(acc);
	}
}
