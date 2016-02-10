package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String username;
	private String password;
	
	private String firstName;
	private String lastName;
	
	private String email;
	
	protected Account() {
	}
	public Account(String firstName, String lastName, String email, String username, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		
		this.email = email;
		
		this.username = username;
		this.password = password;
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
