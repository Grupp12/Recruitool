/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Colin
 */
public interface AccountDTO {
	public String getFirstName();
	public String getLastName();
	public String getEmail();
	public String getUsername();
	public ApplicationDTO getApplication();
	public Role getRole();
}
