package controller;

import model.ValidationException;
import integration.AccountDao;
import integration.ApplicationDao;
import integration.EntityExistsException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import model.Account;
import model.AccountDTO;
import model.Application;
import model.Competence;
import model.pdf.ApplicationPDF;
import view.ApplicationFormDTO;
import view.RegisterFormDTO;

/**
 * The {@code Controller} is responsible for all business logic.
 */
@TransactionAttribute(REQUIRES_NEW)
@Stateless
public class Controller {

	@EJB
	private AccountDao accountDao;
	@EJB
	private ApplicationDao applicationDao;

	/**
	 * Registers a new {@code Account}.
	 *
	 * @param registerForm the new account info.
	 *
	 * @throws ValidationException if account data is invalid.
	 * @throws EntityExistsException if account already exists.
	 */
	public void register(RegisterFormDTO registerForm) 
			throws ValidationException, EntityExistsException {
		Account acc = new Account(registerForm);

		accountDao.persistAccount(acc);
	}

	/**
	 * Create a new application.
	 *
	 * @param applicationForm the application info.
	 * @param account the account associated with the application.
	 * @throws ParseException if wrong date format.
	 */
	public void submitApplication(ApplicationFormDTO applicationForm, AccountDTO account) throws ParseException {
		((Account)account).createApplication(applicationForm, applicationDao);
	}

	/**
	 * @return Gets a list of all available competences
	 */
	public List<Competence> getAllCompetences() {
		return applicationDao.getAllCompetences();
	}

	/**
	 * @param username
	 * @return Gets the account with the specified username from the database.
	 */
	public AccountDTO retrieveAccount(String username) {
		return accountDao.getAccount(username);
	}
	
	/**
	 * @param account
	 * @return ApplicationPDF object for the account's application.
	 * @throws IOException 
	 */
	public ApplicationPDF getApplicationPDF(AccountDTO account) throws IOException {
		return ((Application)((Account)account).getApplication()).generatePDF();
	}
	
}
