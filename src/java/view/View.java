package view;

import controller.Controller;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import integration.EntityExistsException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import model.Account;
import model.Competence;
import model.pdf.ApplicationPDF;

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
	private String formMessage;

	/**
	 * Returns the object representing the register form.
	 *
	 * @return the register form object.
	 */
	public RegisterForm getRegisterForm() {
		return registerForm;
	}
	
	/**
	 * Registers a new account with the data currently in the register form.
	 *
	 * @return the result with value unhandledError if error occurred else a
	 * empty string. Error page will be shown if return value is unhandledError.
	 */
	public String register() {
		String result;
		formMessage = null;
		try {
			controller.register(registerForm);
			result = "submitapplication.xhtml?faces-redirect=true";

			formMessage = "Your account has been created!";

			// Reset the form
			registerForm = new RegisterForm();
		}
		catch (Throwable ex) {
			result = handleException(ex);
		}
		return result;
	}
	
	/**
	 * Returns the object representing the application form.
	 *
	 * @return the application form object.
	 */
	public ApplicationForm getApplicationForm() {
		return applicationForm;
	}
	
	/**
	 * NOTE: TEMPORARY!
	 */
	public String getApplicationStatus() {
		String applStatus = "";
		for (AvailabilityFormDTO avF : applicationForm.getAvailabilities()){
			applStatus += "Availability: " + avF.getFrom() + " - " + avF.getTo() + "\n";
		}
		for (CompetenceProfileFormDTO compF : applicationForm.getCompetences()){
			applStatus += "CompetenceProfile: " + compF.getCompetence() + ", years of experience: " + compF.getYearsOfExperience() + "\n";
		}
		
		return applStatus;
	}

	/**
	 * Create a new application with the data currently in the application form
	 * and the account that is logged in.
	 */
	public String submitApplication() {
		String result = "";
		try {
			tryLogin();
			controller.submitApplication(applicationForm, account);
			formMessage = "Application submitted";
			applicationForm = new ApplicationForm();
		}
		catch (Throwable ex) {
			result = handleException(ex);
		}
		
		return result;
	}

	public String downloadApplicationPDF() {
		try {
			tryLogin();
		} catch (AuthenticationException ex) {
			Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
			return handleException(ex);
		}
		
		try (ApplicationPDF applPdf = new ApplicationPDF(account.getApplication()))
		{
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();

			/* Some JSF component library or some Filter might have set some
			 *headers in the buffer beforehand. We want to get rid of them,
			 * else it may collide.
			 */
			ec.responseReset();
			// Set content type to PDF file
			ec.setResponseContentType("application/pdf");
			// Specify filename
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"application-"
					+ account.getFirstName() + "_" + account.getLastName()
					+ "-" + account.getApplication().getTimeOfRegistration().toString().replace(':', '_')
					+ ".pdf\"");

			// Write pdf to response stream
			applPdf.save(ec.getResponseOutputStream());

			fc.responseComplete();
		}
		catch (Throwable ex) {
			Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
			return handleException(ex);
		}
		
		return "";
	}
	
	/**
	 * Retrieves all available competences.
	 * 
	 * @return a list of all competences.
	 */
	public List<Competence> getAllCompetences() {
		return controller.getAllCompetences();
	}
	
	private String handleException(Throwable ex) {
		String errorMessage = "";
		if (ex instanceof EntityExistsException) {
			formMessage = ex.getMessage();
		}
		else if(ex instanceof AuthenticationException) {
			formMessage = "You are not logged in!";
		}
		else if(ex instanceof ParseException) {
			formMessage = "Wrong date format";
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
	
	private void tryLogin() throws AuthenticationException {
		if (account == null) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String currentUser = facesContext.getExternalContext().getRemoteUser();
			if (currentUser != null) {
				account = controller.retrieveAccount(currentUser);
			} else {
				throw new AuthenticationException("Could not get current user");
			}
		}
	}
}
