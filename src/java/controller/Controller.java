package controller;

import model.ValidationException;
import integration.AccountDao;
import integration.ApplicationDao;
import integration.EntityExistsException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import model.account.Account;
import model.application.Application;
import model.application.Availability;
import model.application.Competence;
import model.application.CompetenceProfile;
import model.application.SimpleDate;
import view.ApplicationFormDTO;
import view.AvailabilityForm;
import view.CompetenceProfileForm;
import view.RegisterFormDTO;

/**
 * The {@code Controller} is responsible for all business logic.
 */
@Stateful
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
	public void register(RegisterFormDTO registerForm) throws ValidationException, EntityExistsException {
		Account acc = new Account(registerForm);
		
		accountDao.persistAccount(acc);
	}
	
	public void submitApplication(ApplicationFormDTO applicationForm, Account account) throws ParseException {
		Application app = new Application(account);
		
		List<CompetenceProfile> competences = new ArrayList();
		List<Availability> availabilities = new ArrayList();
		
		for (AvailabilityForm avF : applicationForm.getAvailabilities()){
			availabilities.add(new Availability(new SimpleDate(avF.getFrom()), new SimpleDate(avF.getTo())));
		}
		for (CompetenceProfileForm compF : applicationForm.getCompetences()){
			int yoe = Integer.parseInt(compF.getYearsOfExperience());
			competences.add(new CompetenceProfile(new Competence(compF.getCompetence()), yoe));
		}
		
		app.setAvailabilities(availabilities);
		app.setCompetences(competences);
		
		applicationDao.persistApplication(app);
	}
}
