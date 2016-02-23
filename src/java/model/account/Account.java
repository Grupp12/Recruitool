package model.account;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import model.application.Application;
import model.ValidationException;

/**
 * {@code Account} represents an account in the application.
 */
@Entity
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 1, message = "First Name can not be empty")
	private String firstName;
	@NotNull
	@Size(min = 1, message = "Last Name can not be empty")
	private String lastName;

	// Must be allowed to be null
	private String ssn;
	
	@NotNull
	@Size(min = 1, message = "E-Mail can not be empty")
	@Pattern(regexp = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]",
			message = "E-Mail is not valid")
	private String email;

	@Id
	@NotNull
	@Size(min = 1, message = "Username can not be empty")
	private String username;
	@NotNull
	@Size(min = 1, message = "Password can not be empty")
	@Column(length = 261) // Hash is always 261 chars long
	private String password;
	
	@Column(name = "ACC_ROLE")
	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	private Application application;
	
	protected Account() {
	}

	/**
	 * Creates a new {@code Account} object.
	 * 
	 * @param firstName The actor's first name.
	 * @param lastName The actor's last name.
	 * @param email The actor's e-mail address.
	 * @param username The username of the account.
	 * @param password The password of the account.
	 */
	public Account(String firstName, String lastName, String email, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;

		this.email = email;

		this.username = username;
		this.password = password;
		
		this.role = Role.APPLICANT;
	}
	/**
	 * This constructor is used by the legacy database migrator.
	 * 
	 * @param firstName The actor's first name.
	 * @param lastName The actor's last name.
	 * @param ssn The actor's social security number.
	 * @param email The actor's e-mail.
	 * @param username The username of the account.
	 * @param password The password of the account.
	 * @param role The actor's role.
	 */
	public Account(String firstName, String lastName, String ssn, String email, String username, String password, String role) {
		this.firstName = firstName;
		this.lastName = lastName;

		this.ssn = ssn;
		
		this.email = email;

		this.username = username;
		this.password = password;
		
		this.role = Role.valueOf(role);
	}

	public Application createApplication(String from, String to) throws ParseException {
		application = new Application(this, from, to);
		return application;
	}
	
	public String getFirstName() {
		return firstName;
	}
        
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Application getApplication() {
		return application;
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (username != null ? username.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Account)) {
			return false;
		}

		Account other = (Account) object;

		return username.equals(other.username);
	}

	@Override
	public String toString() {
		return String.format("User[ firstName=%s, lastName=%s ]", firstName, lastName);
	}
	
	/**
	 * Validates the data in this {@code Account} object.
	 * 
	 * @throws ValidationException If this account contains invalid data.
	 */
	public void validate() throws ValidationException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<Account>> constraintViolations = validator.validate( this );
		if (!constraintViolations.isEmpty()) {
			StringBuilder violationsStr = new StringBuilder();
			for (ConstraintViolation<?> violation : constraintViolations) {
				violationsStr.append(violation.getMessage()).append('\n');
			}
			throw new ValidationException(violationsStr.toString());
		}
	}
}
