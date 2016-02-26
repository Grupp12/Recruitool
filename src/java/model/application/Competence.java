package model.application;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Describes a competence that an applicant can have.
 */
@Entity
public class Competence implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	
	@Column(name = "NAME")
	private String name;

	protected Competence() {
	}
	
	/**
	 * Creates a new {@code Competence} with the specified name.
	 * 
	 * @param name the name of the competence
	 */
	public Competence(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof Competence))
			return false;
		
		Competence other = (Competence) object;
		
		return name.equals(other.name);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString() {
		return String.format("Competence[ name=%s ]", name);
	}
}
