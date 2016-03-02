package integration;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import model.Application;
import model.Competence;

/**
 * DAO for handling Applications in the database using JPA
 */
@Stateless
public class ApplicationDao {
	@PersistenceContext(unitName = "RecruitoolPU")
	EntityManager em;

	@PostConstruct
	private void init() {
		String[] competenceNames = {
			"Java", "C++"
		};
		
		for (String compName : competenceNames) {
			persistCompetence(new Competence(compName));
		}
	}
	
	/**
	 * Persist application to database through JPA
	 */
	public void persistApplication(Application app) {
		em.persist(app);
	}
	
	/**
	 * Persist competence to database through JPA
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
