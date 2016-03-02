package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * Contains information about one application
 * submitted by an applicant.
 */
@Entity
public class Application implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;

	@NotNull
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ACC_ID")
	private Account account;

	@NotNull
	@OneToMany(mappedBy = "application", fetch = FetchType.LAZY,
			cascade = { CascadeType.ALL })
	private List<Availability> availabilities;
	
	@NotNull
	@OneToMany(mappedBy = "application", fetch = FetchType.LAZY,
			cascade = { CascadeType.ALL })
	private List<CompetenceProfile> competences;
	
	@NotNull
	@Column(name = "TIME_OF_REG")
	private Timestamp timeOfRegistration;
	
	@NotNull
	@Column(name = "APPL_STATUS")
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;
	
	protected Application() {
	}
	
	/**
	 * Creates a new {@code Application} submitted by an applicant.
	 * 
	 * @param account the applicant's account.
	 * @param timeOfReg the time of registration.
	 */
	Application(Account account, Timestamp timeOfReg) {
		this.account = account;
		
		this.timeOfRegistration = timeOfReg;
		
		status = ApplicationStatus.SUBMITTED;
	}
	
	/**
	 * Creates a new application for this account.
	 * 
	 * @param from the from-date.
	 * @param to the to-date.
	 */
	public void createAvailability(Date from, Date to) {
		if (availabilities == null)
			availabilities = new ArrayList<>();
		
		availabilities.add(new Availability(this, from, to));
	}
	
	/**
	 * Retrieves a list of all availabilities associated with this application.
	 * 
	 * @return the application's availabilities.
	 */
	public List<Availability> getAvailabilities() {
		return availabilities;
	}
	
	/**
	 * Creates a new competence profile for this application.
	 * 
	 * @param competence the competence.
	 * @param yearsOfExp years of experience.
	 */
	public void createCompetenceProfile(Competence competence, BigDecimal yearsOfExp) {
		if (competences == null)
			competences = new ArrayList<>();
		
		competences.add(new CompetenceProfile(this, competence, yearsOfExp));
	}
	
	/**
	 * Retrieves a list of all competences associated with this application.
	 * 
	 * @return the application's competence profiles.
	 */
	public List<CompetenceProfile> getCompetences() {
		return competences;
	}
	
	public Timestamp getTimeOfRegistration() {
		return timeOfRegistration;
	}
	
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	public ApplicationStatus getStatus() {
		return status;
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
		if (object == null || !(object instanceof Application))
			return false;
		
		Application other = (Application) object;
		
		return id == other.id;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder("Application[ ");
		
		output.append(String.format("account=%s", account));
		
		output.append(", competences={ ");
		for (CompetenceProfile competence : competences) {
			output.append("\n\t");
			output.append(competence.toString());
		}
		output.append(" }");
		
		output.append(", availabilities={ ");
		for (Availability availability : availabilities) {
			output.append("\n\t");
			output.append(availability.toString());
		}
		output.append(" }");
		
		output.append(" ]");
		return output.toString();
	}
}
