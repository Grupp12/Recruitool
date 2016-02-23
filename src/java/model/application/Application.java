package model.application;

import model.account.Account;
import java.io.Serializable;
import java.text.ParseException;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Application implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false, mappedBy = "application")
	private Account account;
	
	@OneToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = false)
	private Availability availability;
	
	@Column(name = "APP_STATUS")
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;
	
	protected Application() {
	}
	
	public Application(Account account, String from, String to) throws ParseException {
		this.account = account;
		
		availability = new Availability(from, to);
	}
	
	public long getId() {
		return id;
	}
	
	public Availability getAvailability() {
		return availability;
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
		return String.format("Application[ id=%d, account=%s, availability=%s ]", id, account, availability);
	}
}
