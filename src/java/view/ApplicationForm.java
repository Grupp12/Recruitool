package view;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.AssertTrue;

/**
 * Holds the application form values.
 */
public final class ApplicationForm implements ApplicationFormDTO {
	
	private CompetenceProfileForm compForm = new CompetenceProfileForm();
	private AvailabilityForm availForm = new AvailabilityForm();
	
	private final List<AvailabilityFormDTO> availabilities = new ArrayList<>();
	private final List<CompetenceProfileFormDTO> competences = new ArrayList<>();
	
	@AssertTrue(message = "You have to add atleast one availability and competence")
	private boolean valid = false;
	
	public CompetenceProfileForm getCompetenceForm() {
		return compForm;
	}
	
	/**
	 * Submits the data in the competence form and then resets the form.
	 */
	public void submitCompetenceForm() {
		competences.add(compForm);
		compForm = new CompetenceProfileForm();
		if(!availabilities.isEmpty()){
			valid = true;
		}
	}
	
	public AvailabilityForm getAvailabilityForm() {
		return availForm;
	}
	
	/**
	 * Submits the data in the availability form and then resets the form.
	 */
	public void submitAvailabilityForm() {
		availabilities.add(availForm);
		availForm = new AvailabilityForm();
		if(!competences.isEmpty()){
			valid = true;
		}
	}

	@Override
	public List<AvailabilityFormDTO> getAvailabilities() {
		return availabilities;
	}

	@Override
	public List<CompetenceProfileFormDTO> getCompetences() {
		return competences;
	}
	
	public boolean getValid() {
		return valid;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
}
