package controller;

import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;
import model.Account;
import view.RegisterFormDTO;

@Stateful
public class Controller {
	@PersistenceContext(unitName = "RecruitoolPU")
	EntityManager em;
	
	/**
	 * Validates the fields in the {@code registerForm}.
	 * A field is valid if it is not equal to {@code null}.
	 * 
	 * @param registerForm the form to validate.
	 * @throws ValidationException if there is a field that is not valid.
	 */
	private void validateRegisterForm(RegisterFormDTO registerForm) throws ValidationException {
		String error = null;
		
		if (registerForm.getFirstName() == null)
			error = "First Name can not be empty";
		else if (registerForm.getLastName() == null)
			error = "Last Name can not be empty";
		else if (registerForm.getEmail() == null)
			error = "Email can not be empty";
		else if (registerForm.getUsername() == null)
			error = "Username can not be empty";
		else if (registerForm.getPassword() == null)
			error = "Password can not be empty";
		
		if (error != null)
			throw new ValidationException(error);
	}
	
	/**
	 * Registers a new {@code Account}.
	 * 
	 * @param registerForm the new account info.
	 * @throws ValidationException if the form has invalid data.
	 */
	public void register(RegisterFormDTO registerForm) throws ValidationException {
		validateRegisterForm(registerForm);
		
		Account acc = new Account(
				registerForm.getFirstName(),
				registerForm.getLastName(),
				registerForm.getEmail(),
				registerForm.getUsername(),
				registerForm.getPassword());
		
		em.persist(acc);
	}
}
