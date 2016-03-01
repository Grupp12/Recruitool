package model.application;

import model.account.Account;
import java.io.Serializable;
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
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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

	@OneToOne(fetch = FetchType.LAZY, optional = false,
			mappedBy = "application", targetEntity = Account.class)
	private Account account;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(
			name = "APPL_COMP",
			joinColumns = {
				@JoinColumn(name = "APPL_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {
				@JoinColumn(name = "COMP_ID", referencedColumnName = "ID", unique = true)}
	)
	private List<CompetenceProfile> competences;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = false)
	@JoinTable(
			name = "APPL_AVAIL",
			joinColumns = {
				@JoinColumn(name = "APPL_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {
				@JoinColumn(name = "AVAIL_ID", referencedColumnName = "ID", unique = true)}
	)
	private List<Availability> availabilities;
	
	@Column(name = "TIME_OF_REG")
	private Timestamp timeOfRegistration;
	
	@Column(name = "APPL_STATUS")
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;
	
	protected Application() {
	}
	
	/**
	 * Creates a new {@code Application} submitted by an applicant.
	 * 
	 * @param account the applicant's account.
	 */
	public Application(Account account) {
		this.account = account;
		
		competences = new ArrayList<>();
		availabilities = new ArrayList<>();
		
		status = ApplicationStatus.SUBMITTED;
	}
	
	public void setCompetences(List<CompetenceProfile> competences) {
		this.competences = new ArrayList<>(competences);
	}
	public CompetenceProfile[] getCompetences() {
		return competences.toArray(new CompetenceProfile[0]);
	}
	
	public void setAvailabilities(List<Availability> availabilites) {
		this.availabilities = new ArrayList<>(availabilities);
	}
	public Availability[] getAvailabilities() {
		return availabilities.toArray(new Availability[0]);
	}
	
	public void setTimeOfRegistration(Timestamp time) {
		this.timeOfRegistration = time;
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
