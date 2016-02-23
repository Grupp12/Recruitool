package controller;

import model.ValidationException;
import integration.AccountDao;
import integration.EntityExistsException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import model.account.Account;
import model.application.ApplicationStatus;
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
	 * @throws ValidationException if account data is invalid.
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

		try {
			System.out.println(acc.createApplication("1970-01-01", "2016-12-31").toString());
		} catch (ParseException ex) {
			Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
		}
		acc.getApplication().setStatus(ApplicationStatus.DECLINED);
		
		accountDao.persistAccount(acc);
	}
}
