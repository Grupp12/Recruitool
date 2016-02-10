package controller;

import integration.AccountDao;
import integration.EntityExistsException;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import model.Account;
import view.RegisterFormDTO;

@Stateful
public class Controller {

	@EJB
	private AccountDao accountDao;

	/**
	 * Registers a new {@code Account}.
	 *
	 * @param registerForm the new account info.
	 *
	 * @throws EntityExistsException if account already exists.
	 */
	public void register(RegisterFormDTO registerForm) throws EntityExistsException {
		Account acc = new Account(
				registerForm.getFirstName(),
				registerForm.getLastName(),
				registerForm.getEmail(),
				registerForm.getUsername(),
				registerForm.getPassword());

		accountDao.persistAccount(acc);
	}
}
