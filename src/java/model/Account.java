package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import static model.Role.APPLICANT;

@Entity
public class Account implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 1, message = "First Name can not be empty")
	private String firstName;
	@NotNull
	@Size(min = 1, message = "Last Name can not be empty")
	private String lastName;

	@NotNull
	@Size(min = 1, message = "E-Mail can not be empty")
	@Pattern(regexp = "[\\w\\.-]*[a-zA-Z0-9_]@[\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]")
	private String email;

	@Id
	@NotNull
	@Size(min = 1, message = "Username can not be empty")
	private String username;
	@NotNull
	@Size(min = 1, message = "Password can not be empty")
	private String password;
	
	@Column(name = "ACC_ROLE")
	@Enumerated(EnumType.STRING)
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
}
