package model.application;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Competence implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String name;

	protected Competence() {
	}
	
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