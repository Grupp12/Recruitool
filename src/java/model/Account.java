package model;

import integration.ApplicationDao;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import security.Crypto;
import view.ApplicationFormDTO;
import view.AvailabilityFormDTO;
import view.CompetenceProfileFormDTO;
import view.RegisterFormDTO;

/**
 * {@code Account} represents an account in the application.
 */
@Entity
public class Account implements Serializable, AccountDTO {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	
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
	
	@OneToOne(fetch = FetchType.LAZY, optional = true,
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
		this.password = Crypto.simpleHash(registerForm.getPassword());
		
		this.role = Role.APPLICANT;
	}

	/**
	 * Creates and persists a new application for this account.
	 * 
	 * @param applicationForm the application form data.
	 * @param applicationDao the application DAO.
	 * 
	 * @throws ParseException if an availability date doesn't match the format yyyy-MM-dd
	 */
	public void createApplication(ApplicationFormDTO applicationForm,
			ApplicationDao applicationDao) throws ParseException {
		List<Availability> availabilities = new ArrayList();
		List<CompetenceProfile> competences = new ArrayList();
		
		for (AvailabilityFormDTO avF : applicationForm.getAvailabilities()){
			SimpleDate from = new SimpleDate(avF.getFrom());
			SimpleDate to = new SimpleDate(avF.getTo());
			
			availabilities.add(new Availability(from, to));
		}
		if (availabilities.isEmpty())
			throw new IllegalStateException("No availabilities have been submitted");
		
		for (CompetenceProfileFormDTO compF : applicationForm.getCompetences()){
			BigDecimal yoe = new BigDecimal(compF.getYearsOfExperience());
			
			Competence comp = applicationDao.getCompetence(compF.getCompetence());
			
			competences.add(new CompetenceProfile(comp, yoe));
		}
		if (competences.isEmpty())
			throw new IllegalStateException("No competences have been submitted");
		
		if (application != null)
			applicationDao.removeApplication(application);
		
		application = new Application(this);
		
		application.setAvailabilities(availabilities);
		application.setCompetences(competences);
		application.setTimeOfRegistration(new SimpleTimestamp());
		
		applicationDao.persistApplication(application);
	}
	
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
	public ApplicationDTO getApplication() {
		return (ApplicationDTO)application;
	}
	
	@Override
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
