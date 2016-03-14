package integration;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.MANDATORY;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import model.Application;
import model.Competence;

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
	 * Persist competence to database through JPA
	 * 
	 * @param competence the competence object to persist
	 */
	public void persistCompetence(Competence competence) {
		em.persist(competence);
	}
	
	public Competence getCompetence(String competenceName) {
		return em.find(Competence.class, competenceName);
	}
	
	public List<Competence> getAllCompetences() {
		TypedQuery<Competence> competences = em.createQuery("SELECT c FROM Competence c", Competence.class);
		return competences.getResultList();
	}
}
