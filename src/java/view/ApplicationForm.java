package view;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.AssertTrue;

/**
 * Manages the forms used to submit applications.
 */
public final class ApplicationForm implements ApplicationFormDTO {
	
	private CompetenceProfileForm compForm = new CompetenceProfileForm();
	private AvailabilityForm availForm = new AvailabilityForm();
	
	private final List<AvailabilityFormDTO> availabilities = new ArrayList<>();
	private final List<CompetenceProfileFormDTO> competences = new ArrayList<>();
	
	@AssertTrue(message = "You have to add atleast one availability and competence")
	private boolean valid = false;
	
	
	/**
	 * Builds the string that shows the current status of the ongoing application
	 * submission. Used by JSF.
	 * 
	 * @return application form status string.
	 */
	public String getApplicationStatus() {
		String applStatus = "";
		for (AvailabilityFormDTO avF : getAvailabilities()){
			applStatus += "Availability: " + avF.getFrom() + " - " + avF.getTo() + "\n";
		}
		for (CompetenceProfileFormDTO compF : getCompetences()){
			applStatus += "CompetenceProfile: " + compF.getCompetence() + ", years of experience: " + compF.getYearsOfExperience() + "\n";
		}
		
		return applStatus;
	}
	
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
