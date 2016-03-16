package model;

import java.io.IOException;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import model.pdf.ApplicationPDF;

/**
 * Contains information about one application
 * submitted by an applicant.
 */
@Entity
public class Application implements Serializable, ApplicationDTO {
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
	private List<CompetenceProfile> competences;
	
	@NotNull
	@OneToMany(mappedBy = "application", fetch = FetchType.LAZY,
			cascade = { CascadeType.ALL })
	private List<Availability> availabilities;
	
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
	 * Creates a new Application submitted by an applicant.
	 * 
	 * @param account the applicant's account.
	 */
	Application(Account account) {
		this.account = account;
		
		competences = new ArrayList<>();
		availabilities = new ArrayList<>();
		
		status = ApplicationStatus.SUBMITTED;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setCompetences(List<CompetenceProfile> competences) {
		this.competences = competences;
		for (CompetenceProfile competence : this.competences) {
			competence.setApplication(this);
		}
	}
	public List<CompetenceProfile> getCompetences() {
		return competences;
	}
	
	public void setAvailabilities(List<Availability> availabilities) {
		this.availabilities = availabilities;
		for (Availability avail : this.availabilities) {
			avail.setApplication(this);
		}
	}
	public List<Availability> getAvailabilities() {
		return availabilities;
	}
	
	public void setTimeOfRegistration(SimpleTimestamp time) {
		this.timeOfRegistration = time;
	}
	@Override
	public SimpleTimestamp getTimeOfRegistration() {
		return new SimpleTimestamp(timeOfRegistration.getTime());
	}
	
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	@Override
	public ApplicationStatus getStatus() {
		return status;
	}
	
	/**
	 * Creates a new PDF object that holds the data from this application object.
	 * @return the created Application PDF object.
	 * @throws IOException if en error occurred when generating the PDF.
	 */
	public ApplicationPDF generatePDF() throws IOException {
		return new ApplicationPDF(this);
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
