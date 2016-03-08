package view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Holds the availability form values.
 */
public class AvailabilityForm implements AvailabilityFormDTO {
	@NotNull
	@Size(min = 1, message = "From can not be empty")
	private String from;
	@NotNull
	@Size(min = 1, message = "To can not be empty")
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
