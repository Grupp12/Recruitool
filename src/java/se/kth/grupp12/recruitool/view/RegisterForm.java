package se.kth.grupp12.recruitool.view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Holds the register form values.
 */
public final class RegisterForm implements RegisterFormDTO {
	
	@NotNull
	@Size(min = 1, message = "First Name can not be empty")
	private String firstName;
	@NotNull
	@Size(min = 1, message = "Last Name can not be empty")
	private String lastName;

	@NotNull
	@Size(min = 1, message = "E-Mail can not be empty")
	@Pattern(regexp = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]",
			message = "E-Mail is not valid")
	private String email;

	@NotNull
	@Size(min = 1, message = "Username can not be empty")
	private String username;
	@NotNull
	@Size(min = 1, message = "Password can not be empty")
	private String password;

	@Override
	public String getFirstName() {
		return firstName;
	}

	@Override
	public String getLastName() {
		return lastName;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
