package view;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the application form values.
 */
public final class ApplicationForm implements ApplicationFormDTO {
	
	private CompetenceProfileForm compForm = new CompetenceProfileForm();
	private AvailabilityForm availForm = new AvailabilityForm();
	
	private List<AvailabilityFormDTO> availabilities;
	private List<CompetenceProfileFormDTO> competences;
	
	ApplicationForm() {
		this.availabilities = new ArrayList();
		this.competences = new ArrayList();
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
	}

	@Override
	public List<AvailabilityFormDTO> getAvailabilities() {
		return availabilities;
	}

	@Override
	public List<CompetenceProfileFormDTO> getCompetences() {
		return competences;
	}
}
