package controller;

import integration.AccountDao;
import integration.EntityExistsException;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.validation.ValidationException;
import model.Account;
import view.RegisterFormDTO;

@Stateful
@SessionScoped
public class Controller {

	@EJB
	private AccountDao accountDao;

	/**
	 * Validates the fields in the {@code registerForm}. A field is valid if it
	 * is not equal to {@code null}.
	 *
	 * @param registerForm the form to validate.
	 * @throws ValidationException if there is a field that is not valid.
	 */
	private void validateRegisterForm(RegisterFormDTO registerForm) throws ValidationException {
		String error = null;

		if (registerForm.getFirstName() == null) {
			error = "First Name can not be empty";
		} else if (registerForm.getLastName() == null) {
			error = "Last Name can not be empty";
		} else if (registerForm.getEmail() == null) {
			error = "Email can not be empty";
		} else if (registerForm.getUsername() == null) {
			error = "Username can not be empty";
		} else if (registerForm.getPassword() == null) {
			error = "Password can not be empty";
		}

		if (error != null) {
			throw new ValidationException(error);
		}
	}

	/**
	 * Registers a new {@code Account}.
	 *
	 * @param registerForm the new account info.
	 * @throws ValidationException if the form has invalid data.
	 * @throws EntityExistsException if account already exists.
	 */
	public void register(RegisterFormDTO registerForm) throws ValidationException, EntityExistsException {
		validateRegisterForm(registerForm);

		Account acc = new Account(
				registerForm.getFirstName(),
				registerForm.getLastName(),
				registerForm.getEmail(),
				registerForm.getUsername(),
				registerForm.getPassword());

		accountDao.persistAccount(acc);
	}
}
