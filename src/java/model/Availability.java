package model;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Contains information about an applicant's
 * availability period.
 */
@Entity
public class Availability implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private long id;
	
	@NotNull
	@Column(name = "FROM_DATE")
	private Date from;
	
	@NotNull
	@Column(name = "TO_DATE")
	private Date to;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, optional = false)
	@JoinColumn(name = "APPL_ID")
	private Application application;
	
	protected Availability() {
	}
	
	/**
	 * Creates a new {@code Availability} period starting at
	 * the {@code from} date and ends at the {@code to} date.
	 * 
	 * @param from
	 * @param to 
	 */
	Availability(Application application, Date from, Date to) {
		this.from = from;
		this.to = to;
	}
	
	public Date getFrom() {
		return from;
	}
	
	public Date getTo() {
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
		/*class ConvertToDateString {
			String convert(Date date) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(date.getTime());
				
				return String.format("%04d-%02d-%02d",
						cal.get(Calendar.YEAR),
						cal.get(Calendar.MONTH) + 1,
						cal.get(Calendar.DAY_OF_MONTH));
			}
		}
		ConvertToDateString converter = new ConvertToDateString();*/
		
		return String.format("Availability[ from=%s, to=%s ]", from, to);
	}
}
