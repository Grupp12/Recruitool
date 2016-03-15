package view;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Holds competence profiles for the application submission.
 */
public class CompetenceProfileForm implements CompetenceProfileFormDTO {
	@NotNull
	@Size(min = 1, message = "Competence can not be empty")
	private String competence;
	@NotNull
	@Size(min = 1, message = "Years of experience can not be empty")
	private String yearsOfExperience;

	@Override
	public String getCompetence() {
		return competence;
	}

	@Override
	public String getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setCompetence(String competence) {
		this.competence = competence;
	}

	public void setYearsOfExperience(String yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}
}
