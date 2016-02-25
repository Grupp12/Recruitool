package model.application;

import model.account.Account;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
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

@Entity
public class Application implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
	private Set<CompetenceProfile> competences;
	
	@Column(name = "APP_STATUS")
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;
	
	protected Application() {
	}
	
	public Application(Account account, Availability availability, Set<CompetenceProfile> competences)
			throws ParseException {
		this.account = account;
		
		this.availability = availability;
		
		this.timeOfRegistration = new Timestamp(System.currentTimeMillis());
		
		this.competences = competences;
		
		status = ApplicationStatus.SUBMITTED;
	}
	
	public Availability getAvailability() {
		return availability;
	}
	
	public Timestamp getTimeOfRegistration() {
		return timeOfRegistration;
	}
	
	public Set<CompetenceProfile> getCompetences() {
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
		
		return other.id == id;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString() {
		return String.format("Application[ account=%s, availability=%s ]", account, availability);
	}
}
