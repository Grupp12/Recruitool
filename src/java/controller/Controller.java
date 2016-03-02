package controller;

import model.ValidationException;
import integration.AccountDao;
import integration.ApplicationDao;
import integration.EntityExistsException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
import model.Account;
import model.Application;
import model.Competence;
import model.SimpleDate;
import view.ApplicationFormDTO;
import view.AvailabilityForm;
import view.CompetenceProfileForm;
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
	 * @return the created account.
	 * 
	 * @throws ValidationException if account data is invalid.
	 * @throws EntityExistsException if account already exists.
	 */
	public Account register(RegisterFormDTO registerForm) throws ValidationException, EntityExistsException {
		Account acc = new Account(registerForm);
		
		accountDao.persistAccount(acc);
		
		return acc;
	}
	
	/**
	 * Create a new application.
	 * 
	 * @param applicationForm the application info.
	 * @param account the account associated with the application. 
	 * @throws ParseException if wrong date format.
	 */
	public void submitApplication(ApplicationFormDTO applicationForm, Account account) throws ParseException {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Application appl = account.createApplication(currentTime);
		
		for (AvailabilityForm avF : applicationForm.getAvailabilities()){
			appl.createAvailability(new SimpleDate(avF.getFrom()), new SimpleDate(avF.getTo()));
		}
		
		for (CompetenceProfileForm compF : applicationForm.getCompetences()){
			BigDecimal yoe = new BigDecimal(compF.getYearsOfExperience());
			Competence comp = applicationDao.getCompetence(compF.getCompetence());
			appl.createCompetenceProfile(comp, yoe);
		}
		
		applicationDao.persistApplication(appl);
	}
	
	/**
	 * Retrieves all available competences.
	 * 
	 * @return all the available competences.
	 */
	public List<Competence> getAllCompetences() {
		return applicationDao.getAllCompetences();
	}
}
