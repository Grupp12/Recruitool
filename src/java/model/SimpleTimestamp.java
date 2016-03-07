package model;

import java.sql.Timestamp;
import java.util.Calendar;

public class SimpleTimestamp extends Timestamp {
	public SimpleTimestamp() {
		super(System.currentTimeMillis());
	}
	
	@Override
	public String toString() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this);
		
		String timeStr = "";
		timeStr += cal.get(Calendar.YEAR);
		timeStr += String.format("-%02d", cal.get(Calendar.MONTH + 1));
		timeStr += String.format("-%02d", cal.get(Calendar.DAY_OF_MONTH));
		timeStr += String.format(":%02d", cal.get(Calendar.HOUR_OF_DAY));
		timeStr += String.format(":%02d", cal.get(Calendar.MINUTE));
		timeStr += String.format(":%02d", cal.get(Calendar.SECOND));
		
		return timeStr;
	}
}
