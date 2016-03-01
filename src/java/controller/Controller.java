package controller;

import model.ValidationException;
import integration.AccountDao;
import integration.ApplicationDao;
import integration.EntityExistsException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import model.Account;
import model.Application;
import model.Availability;
import model.Competence;
import model.CompetenceProfile;
import model.SimpleDate;
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
	public Account register(RegisterFormDTO registerForm) throws ValidationException, EntityExistsException {
		Account acc = new Account(registerForm);
		
		accountDao.persistAccount(acc);
		
		return acc;
	}
	
	public void submitApplication(ApplicationFormDTO applicationForm, Account account) throws ParseException {
		List<CompetenceProfile> competences = new ArrayList();
		List<Availability> availabilities = new ArrayList();
		
		for (AvailabilityForm avF : applicationForm.getAvailabilities()){
			availabilities.add(new Availability(new SimpleDate(avF.getFrom()), new SimpleDate(avF.getTo())));
		}
		for (CompetenceProfileForm compF : applicationForm.getCompetences()){
			double dYoe = Double.parseDouble(compF.getYearsOfExperience());
			BigDecimal yoe = BigDecimal.valueOf(dYoe);
			
			Competence comp = applicationDao.getCompetence(compF.getCompetence());
			
			competences.add(new CompetenceProfile(comp, yoe));
		}
		
		Application appl = account.createApplication(competences, availabilities);
		applicationDao.persistApplication(appl);
	}
}
