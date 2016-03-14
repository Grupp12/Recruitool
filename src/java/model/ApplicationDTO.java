/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author Colin
 */
public interface ApplicationDTO {
	public SimpleTimestamp getTimeOfRegistration();
	public ApplicationStatus getStatus();
}
