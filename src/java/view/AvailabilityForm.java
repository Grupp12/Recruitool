package view;

/**
 * Holds the availability form values.
 */
public class AvailabilityForm implements AvailabilityFormDTO{
	private String from;
	private String to;

	@Override
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
}
