package controller;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import model.Account;

@Stateless
public class Controller {
	@PersistenceContext(unitName = "RecruitoolPU")
	EntityManager em;
	
	public void register(String firstName, String lastName, String email, String username, String password)
			throws Exception {
		if (em.find(Account.class, username) != null)
			throw new Exception("Account already exists! username=" + username);
		
		Account acc = new Account(firstName, lastName, email, username, password);
		
		em.persist(acc);
	}
}
