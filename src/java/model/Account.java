package model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import static model.Role.APPLICANT;
import org.hibernate.validator.constraints.Email;

@Entity
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Size(min = 1)
	private String username;
	@NotNull
	@Size(min = 1)
	private String password;

	@NotNull
	@Size(min = 1)
	private String firstName;
	@NotNull
	@Size(min = 1)
	private String lastName;

	@NotNull
	@Size(min = 1)
	@Pattern(regexp = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
	private String email;

	private Role role;

	protected Account() {
	}

	public Account(String firstName, String lastName, String email, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;

		this.email = email;

		this.username = username;
		this.password = password;

		this.role = APPLICANT;
		
		
	}

	public String getFirstName() {
		return firstName;
	}
        
	public String getUsername() {
		return username;
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
		return String.format("model.User[ firstName=%s, lastName=%s ]", firstName, lastName);
	}
	
	private void validate() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<Account>> constraintViolations = validator.validate( this );
		
		
		
	}
	
}
