package model.application;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	public Availability(String from, String to) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		Date fromDate = dateFormat.parse(from);
		Date toDate = dateFormat.parse(to);
		
		this.from = new Timestamp(fromDate.getTime());
		this.to = new Timestamp(toDate.getTime());
	}
	
	public long getId() {
		return id;
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
		
		return other.id == id;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public String toString() {
		return String.format("Availability[ id=%d, from=%s, to=%s ]", id, from.toString(), to.toString());
	}
	
}
