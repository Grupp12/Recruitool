/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.List;

/**
 * DTO in the form of an interface to the application form.
 */
public interface ApplicationFormDTO {
	public List<AvailabilityFormDTO> getAvailabilities();
	public List<CompetenceProfileFormDTO> getCompetences();
}
