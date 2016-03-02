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
	
	public void addCompetenceProfile() {
		competences.add(compForm);
		compForm = new CompetenceProfileForm();
	}
	
	public AvailabilityForm getAvailabilityForm() {
		return availForm;
	}
	
	public void addAvailability() {
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
