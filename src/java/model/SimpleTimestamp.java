package model;

import java.sql.Timestamp;

public class SimpleTimestamp extends Timestamp {
	public SimpleTimestamp() {
		super(System.currentTimeMillis());
	}
	
	public SimpleTimestamp(long time) {
		super(time);
	}
	
	@Override
	public String toString() {
		String timeStr = super.toString();
		
		// Remove milliseconds
		timeStr = timeStr.substring(0, timeStr.lastIndexOf('.'));
		
		return timeStr;
	}
}
