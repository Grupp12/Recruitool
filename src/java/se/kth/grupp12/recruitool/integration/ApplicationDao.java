package se.kth.grupp12.recruitool.integration;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.MANDATORY;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import se.kth.grupp12.recruitool.model.Application;
import se.kth.grupp12.recruitool.model.Competence;

/**
 * DAO for handling Applications in the database using JPA
 */
@TransactionAttribute(MANDATORY)
@Stateless
public class ApplicationDao {
	@PersistenceContext(unitName = "RecruitoolPU")
	EntityManager em;
	
	/**
	 * Persist application to database through JPA
	 * 
	 * @param app the application object to persist
	 */
	public void persistApplication(Application app) {
		em.persist(app);
	}
	
	/**
	 * Removes the application object from the database.
	 * @param app the application to remove.
	 */
	public void removeApplication(Application app) {
		em.remove(em.merge(app));
	}
	
	/**
	 * Persist competence to database through JPA
	 * 
	 * @param competence the competence object to persist
	 */
	public void persistCompetence(Competence competence) {
		em.persist(competence);
	}
	
	/**
	 * Returns a reference to the requested Competence object.
	 * @param competenceName the name of the requested competence.
	 * @return the requested Competence object.
	 */
	public Competence getCompetence(String competenceName) {
		return em.find(Competence.class, competenceName);
	}
	
	/**
	 * Retrieves all the competences in the database.
	 * @return a list of all competences.
	 */
	public List<Competence> getAllCompetences() {
		TypedQuery<Competence> competences = em.createQuery("SELECT c FROM Competence c", Competence.class);
		return competences.getResultList();
	}
}
