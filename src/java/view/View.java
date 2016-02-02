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
	
	private final LoginForm loginForm = new LoginForm();
	
	public LoginForm getLoginForm() {
		return loginForm;
	}
	
	public void register() {
		try {
			controller.register(loginForm.getFirstName(), loginForm.getLastName(),
					loginForm.getEmail(), loginForm.getUsername(), loginForm.getPassword());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
