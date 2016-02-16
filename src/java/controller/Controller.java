package controller;

import model.ValidationException;
import integration.AccountDao;
import integration.EntityExistsException;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import model.Account;
import security.Crypto;
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
	 * @throws controller.ValidationException
	 * @throws EntityExistsException if account already exists.
	 */
	public void register(RegisterFormDTO registerForm) throws ValidationException, EntityExistsException {
		String hashedPassword = Crypto.generateHash(registerForm.getPassword());
		Account acc = new Account(
				registerForm.getFirstName(),
				registerForm.getLastName(),
				registerForm.getEmail(),
				registerForm.getUsername(),
				hashedPassword);

		accountDao.persistAccount(acc);
	}
}
