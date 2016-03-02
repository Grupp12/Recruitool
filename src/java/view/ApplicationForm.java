package view;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the application form values.
 */
public final class ApplicationForm implements ApplicationFormDTO{
	
	private List<AvailabilityForm> availabilities;
	private List<CompetenceProfileForm> competences;
	
	ApplicationForm() {
		this.availabilities = new ArrayList();
		this.competences = new ArrayList();
	}
	
	public void addCompetenceProfileForm(CompetenceProfileForm cpf) {
		competences.add(cpf);
	}
	
	public void addAvailabilityForm(AvailabilityForm af) {
		availabilities.add(af);
	}

	@Override
	public List<AvailabilityForm> getAvailabilities() {
		return availabilities;
	}

	@Override
	public List<CompetenceProfileForm> getCompetences() {
		return competences;
	}

	public void setAvailabilities(List<AvailabilityForm> availabilities) {
		this.availabilities = availabilities;
	}

	public void setCompetences(List<CompetenceProfileForm> competences) {
		this.competences = competences;
	}
	
}
