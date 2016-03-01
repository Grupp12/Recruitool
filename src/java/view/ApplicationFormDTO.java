/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.util.List;

/**
 *
 * @author Simon Ciesluk
 */
public interface ApplicationFormDTO {
	public List<AvailabilityForm> getAvailabilities();
	public List<CompetenceProfileForm> getCompetences();
}
