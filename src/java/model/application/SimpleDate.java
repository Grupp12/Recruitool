package model.application;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * {@code SimpleDate} only serves to provide useful
 * constructors for the {@code Timestamp} class.
 */
public class SimpleDate extends Timestamp implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new {@code SimpleDate} set to the current
	 * date and time.
	 */
	public SimpleDate() {
		super(System.currentTimeMillis());
	}
	
	/**
	 * Constructs a new {@code SimpleDate} set to the date sent as a parameter.
	 * The date must be in the format yyyy-MM-dd.
	 * 
	 * @param date The date in the format yyyy-MM-dd
	 * @throws ParseException if the date doesn't match the format yyyy-MM-dd
	 */
	public SimpleDate(String date) throws ParseException {
		super(getTimeFromDate(date));
	}
	
	private static long getTimeFromDate(String date) throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		java.util.Date fromDate = dateFormat.parse(date);
		
		return fromDate.getTime();
	}
}
