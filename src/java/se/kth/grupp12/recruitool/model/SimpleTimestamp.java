package se.kth.grupp12.recruitool.model;

import java.sql.Timestamp;

public class SimpleTimestamp extends Timestamp {
	public SimpleTimestamp() {
		super(System.currentTimeMillis());
	}
	
	public SimpleTimestamp(long time) {
		super(time);
	}
	
	/**
	 * Timestamp without milliseconds.
	 * @return 
	 */
	@Override
	public String toString() {
		String timeStr = super.toString();
		
		timeStr = timeStr.substring(0, timeStr.lastIndexOf('.'));
		
		return timeStr;
	}
}
