/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 * Holds the competence profile form values.
 */
public class CompetenceProfileForm implements CompetenceProfileFormDTO {
	private String competence;
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
