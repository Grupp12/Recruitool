package model.application;

import model.account.Account;
import java.io.Serializable;
import java.sql.Timestamp;
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
	@Column(name = "APPL_ID")
	private long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false,
			mappedBy = "application", targetEntity = Account.class)
	private Account account;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = false)
	private Availability availability;
	
	private Timestamp timeOfRegistration;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
	@JoinTable(
			name = "APPL_COMP",
			joinColumns = {
				@JoinColumn(name = "APPL_ID", referencedColumnName = "APPL_ID")},
			inverseJoinColumns = {
				@JoinColumn(name = "COMP_ID", referencedColumnName = "COMP_ID", unique = true)}
	)
	private List<CompetenceProfile> competences;
	
	@Column(name = "APP_STATUS")
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;
	
	protected Application() {
	}
	
	/**
	 * Creates a new {@code Application} submitted by an applicant.
	 * 
	 * @param account the applicant's account.
	 * @param availability the applicant's availability period.
	 * @param competences the applicant's competences.
	 */
	public Application(Account account, Availability availability, List<CompetenceProfile> competences) {
		this.account = account;
		
		this.availability = availability;
		
		this.timeOfRegistration = new SimpleDate();
		
		this.competences = competences;
		
		status = ApplicationStatus.SUBMITTED;
	}
	
	public Availability getAvailability() {
		return availability;
	}
	
	public Timestamp getTimeOfRegistration() {
		return timeOfRegistration;
	}
	
	public List<CompetenceProfile> getCompetences() {
		return competences;
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
		output.append(String.format("account=%s, availability=%s", account, availability));
		output.append(", competences={ ");
		for (CompetenceProfile competence : competences) {
			output.append("\n\t");
			output.append(competence.toString());
		}
		output.append(" } ]");
		return output.toString();
	}
}
