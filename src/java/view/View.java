package view;

import controller.Controller;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import util.Log;

@Named("myView")
@SessionScoped
public class View implements Serializable {
	@EJB
	private Controller controller;
	
	private RegisterForm registerForm = new RegisterForm();
	
	private String formMessage;
	
	public RegisterForm getLoginForm() {
		return registerForm;
	}
	
	@Log
	public void register() {
		formMessage = null;
		try {
			controller.register(registerForm);
			
			// Reset the form
			registerForm = new RegisterForm();
			
			formMessage = "Your account has been created!";
		} catch (Exception ex) {
			formMessage = ex.getMessage();
		}
	}
	
	public String getFormMessage() {
		return formMessage;
	}
}
