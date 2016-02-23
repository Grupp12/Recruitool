package model;

import java.io.Serializable;
import javax.persistence.Entity;
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

	@OneToOne(optional = false, mappedBy = "application", fetch = FetchType.LAZY)
	private Account account;
	
	protected Application() {
	}
	
	public Application(Account account) {
		this.account = account;
	}
	
	public Long getId() {
		return id;
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
		return String.format("model.Application[ id=%d, account=%s ]", id, account.toString());
	}
}
