package model.application;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Availability implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "FROM_DATE")
	private Timestamp from;
	
	@Column(name = "TO_DATE")
	private Timestamp to;
	
	protected Availability() {
	}
	
	public Availability(Timestamp from, Timestamp to) throws ParseException {
		this.from = from;
		this.to = to;
	}
	
	public Timestamp getFrom() {
		return from;
	}
	
	public Timestamp getTo() {
		return to;
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
		if (object == null || !(object instanceof Availability))
			return false;
		
		Availability other = (Availability) object;
		
		return id == other.id;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString() {
		return String.format("Availability[ from=%s, to=%s ]", from.toString(), to.toString());
	}
	
}
