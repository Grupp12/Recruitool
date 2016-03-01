package integration;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import model.application.Application;
import model.application.Competence;

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

	public void persistApplication(Application app) {
		em.persist(app);
	}
	
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
