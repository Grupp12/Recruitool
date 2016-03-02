package view;

import controller.Controller;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import integration.EntityExistsException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Competence;

/**
 * View class to be used by JSF. Handles basic view logic and calls the 
 * controller to perform application operations.
 */
@Named("myView")
@SessionScoped
public class View implements Serializable {
	@EJB
	private Controller controller;
	
	private Account account;
	
	private RegisterForm registerForm = new RegisterForm();
	
	private ApplicationForm applicationForm = new ApplicationForm();
	
	private CompetenceProfileForm cpf = new CompetenceProfileForm();
	
	private AvailabilityForm af = new AvailabilityForm();
	
	private String formMessage;
	
	/**
	 * Returns the object representing the register form.
	 * 
	 * @return the register form object.
	 */
	public RegisterForm getRegisterForm() {
		return registerForm;
	}

	public CompetenceProfileForm getCpf() {
		return cpf;
	}

	public AvailabilityForm getAf() {
		return af;
	}
	
	/**
	 * Registers a new account with the data currently in the register form.
	 * @return string(result) with value unhandledError if error occurred else
	 * a empty string. Error page will be shown if return value is unhandledError.
	 */
	public String register() {
		String result;
		formMessage = null;
		try {
			account = controller.register(registerForm);
			result = "submitapplication";
			
			formMessage = "Your account has been created!";
			
			// Reset the form
			registerForm = new RegisterForm();
		} catch (Throwable ex) {
			result = handleException(ex);
		}
		return result;
	}
	
	/**
	 * Add a new competence profile to the application.
	 */
	public void addCompetenceProfile() {
		applicationForm.addCompetenceProfileForm(cpf);
		// Reset the form
		cpf = new CompetenceProfileForm();
		showApplicationStatus();
	}
	
	/**
	 * Add a new availability to the application.
	 */
	public void addAvailability() {
		applicationForm.addAvailabilityForm(af);
		// Reset the form
		af =  new AvailabilityForm();
		showApplicationStatus();
	}
	
	private void showApplicationStatus() {
		formMessage = "";
		for (AvailabilityForm avF : applicationForm.getAvailabilities()){
			formMessage += "Availability: " + avF.getFrom() + " - " + avF.getTo() + "\n";
		}
		for (CompetenceProfileForm compF : applicationForm.getCompetences()){
			formMessage += "CompetenceProfile: " + compF.getCompetence() + ", years of experience: " + compF.getYearsOfExperience() + "\n";
		}
	}
	
	/**
	 * Create a new application with the data currently in the application form 
	 * and the account that is logged in.
	 */
	public void submitApplication() {
		try {
			controller.submitApplication(applicationForm, account);
			formMessage = "Application submitted";
		} catch (ParseException ex) {
			Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
			formMessage = "Wrong date format";
		}
		applicationForm = new ApplicationForm();
	}
	
	public List<Competence> getAllCompetences() {
		return controller.getAllCompetences();
	}
	
	private String handleException (Throwable ex) {
		String errorMessage = "";
		if(ex instanceof EntityExistsException) {
			formMessage = ex.getMessage();
		}
		else {
			errorMessage = "unhandledError";
		}
		
		return errorMessage;
	}
	
	/**
	 * Returns the latest message generated by the input form.
	 * 
	 * @return the registration message.
	 */
	public String getFormMessage() {
		return formMessage;
	}
}
