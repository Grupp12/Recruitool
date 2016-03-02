package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
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
import model.ValidationException;
import security.Crypto;
import view.RegisterFormDTO;

/**
 * {@code Account} represents an account in the application.
 */
@Entity
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@Size(min = 1, message = "First Name can not be empty")
	@Column(name = "FIRSTNAME")
	private String firstName;
	
	@NotNull
	@Size(min = 1, message = "Last Name can not be empty")
	@Column(name = "LASTNAME")
	private String lastName;

	@NotNull
	@Size(min = 1, message = "E-Mail can not be empty")
	@Pattern(regexp = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]",
			message = "E-Mail is not valid")
	@Column(name = "EMAIL")
	private String email;

	@Id
	@NotNull
	@Size(min = 1, message = "Username can not be empty")
	@Column(name = "USERNAME")
	private String username;
	
	@NotNull
	@Size(min = 1, message = "Password can not be empty")
	@Column(name = "PASSWORD", length = 261) // Hash is always 261 chars long
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ACC_ROLE")
	private Role role;

	@Column(name = "SSN")
	private String ssn;
	
	@OneToOne(fetch = FetchType.LAZY, /*cascade = { CascadeType.ALL },*/ optional = true,
			mappedBy = "account", targetEntity = Application.class)
	private Application application;
	
	protected Account() {
	}

	/**
	 * Creates a new {@code Account} object.
	 * 
	 * @param registerForm the form input where account info is stored
	 */
	public Account(RegisterFormDTO registerForm) {
		this.firstName = registerForm.getFirstName();
		this.lastName = registerForm.getLastName();

		this.email = registerForm.getEmail();

		this.username = registerForm.getUsername();
		this.password = Crypto.generateHash(registerForm.getPassword());
		
		this.role = Role.APPLICANT;
	}

	public Application createApplication(List<CompetenceProfile> competences, List<Availability> availabilities) {
		application = new Application(this);
		application.setAvailabilities(availabilities);
		application.setCompetences(competences);
		application.setTimeOfRegistration(new Timestamp(System.currentTimeMillis()));
		
		return application;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return firstName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getUsername() {
		return username;
	}
	
	public boolean validatePassword(String password) {
		return Crypto.validateHash(password, this.password);
	}
	
	public Application getApplication() {
		return application;
	}
	
	public Role getRole() {
		return role;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (username != null ? username.hashCode() : 0);
		return hash;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Account)) {
			return false;
		}

		Account other = (Account) object;

		return username.equals(other.username);
	}

	/**
	 * @inheritDoc
	 */
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
