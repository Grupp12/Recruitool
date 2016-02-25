package model.application;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class CompetenceProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "COMP_ID")
	private long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false,
			cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	private Competence competence;
	
	private int yearsOfExperience;
	
	protected CompetenceProfile() {
	}
	
	public CompetenceProfile(Competence competence, int yearsOfExperience) {
		this.competence = competence;
		this.yearsOfExperience = yearsOfExperience;
	}
	
	public Competence getCompetence() {
		return competence;
	}
	
	public int getYearsOfExperience() {
		return yearsOfExperience;
	}
	
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof CompetenceProfile))
			return false;
		
		CompetenceProfile other = (CompetenceProfile) object;
		
		return id == other.id;
	}

	@Override
	public String toString() {
		return String.format("CompetenceProfile[ competence=%s, yearsOfExperience=%d ]",
				competence, yearsOfExperience);
	}
	
}
