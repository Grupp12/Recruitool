package view;

import controller.Controller;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;

@Named("myView")
@SessionScoped
public class View implements Serializable {
	@EJB
	private Controller controller;
	
	private RegisterForm registerForm = new RegisterForm();
	
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
	 */
	public void register() {
		formMessage = null;
		try {
			controller.register(registerForm);
			
			// Reset the form
			registerForm = new RegisterForm();
			
			formMessage = "Your account has been created!";
		} catch (Exception ex) {
			formMessage = "Error: " + ex.getMessage();
		}
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
