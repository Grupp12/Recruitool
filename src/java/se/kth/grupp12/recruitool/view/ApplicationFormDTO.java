package se.kth.grupp12.recruitool.view;

import java.util.List;

public interface ApplicationFormDTO {
	public List<AvailabilityFormDTO> getAvailabilities();
	public List<CompetenceProfileFormDTO> getCompetences();
}
