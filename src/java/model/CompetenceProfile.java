package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * Contains information about an applicant's
 * years of experience of one competence.
 */
@Entity
public class CompetenceProfile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY, optional = false,
			cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "COMP_ID")
	private Competence competence;
	
	@NotNull
	@Column(name = "YEARS_OF_EXP", precision = 4, scale = 2)
	private BigDecimal yearsOfExperience;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = false)
	@JoinColumn(name = "APPL_ID")
	private Application application;
	
	protected CompetenceProfile() {
	}
	
	/**
	 * Creates a new {@code CompetenceProfile} with the
	 * specified {@code Competence} and years of experience.
	 * 
	 * @param competence
	 * @param yearsOfExperience 
	 */
	public CompetenceProfile(Competence competence, BigDecimal yearsOfExperience) {
		this.competence = competence;
		this.yearsOfExperience = yearsOfExperience;
	}
	
	public Competence getCompetence() {
		return competence;
	}
	
	public BigDecimal getYearsOfExperience() {
		return yearsOfExperience;
	}
	
	void setApplication(Application appl) {
		this.application = appl;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof CompetenceProfile))
			return false;
		
		CompetenceProfile other = (CompetenceProfile) object;
		
		return id == other.id;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString() {
		return String.format("CompetenceProfile[ competence=%s, yearsOfExperience=%d ]",
				competence, yearsOfExperience);
	}
}
