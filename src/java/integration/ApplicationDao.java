/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package integration;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.application.Application;

/**
 *
 * @author Simon Ciesluk
 */
@Stateless
public class ApplicationDao {
	@PersistenceContext(unitName = "RecruitoolPU")
	EntityManager em;
	
	public void persistApplication(Application app) {
		em.persist(app);
	}	
}
