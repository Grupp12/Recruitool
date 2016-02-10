package integration;

import controller.ValidationException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import model.Account;

@Stateless
public class AccountDao {

	@PersistenceContext(unitName = "RecruitoolPU")
	EntityManager em;

	/**
	 * Persist account to database.
	 *
	 * @param acc the account to persist.
	 * 
	 * @throws controller.ValidationException
	 * @throws EntityExistsException if account already exists.
	 */
	public void persistAccount(Account acc) throws ValidationException, EntityExistsException {
		try {
			Account found = em.find(Account.class, acc.getUsername());
			if (found != null) {
				throw new EntityExistsException("Account with username '" + acc.getUsername() + "' already exists.");
			}
			em.persist(acc);
		} catch (ConstraintViolationException ex) {
			StringBuilder violationsStr = new StringBuilder();
			for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
				String property = violation.getPropertyPath().toString();
				
				String violationMsg;
				
				propertyIf:if (property.equals("email") && violation.getInvalidValue().toString().length() > 0) {
					violationMsg = violation.getInvalidValue() + " is not a valid E-Mail";
				} else {
					switch (property) {
					case "firstName":
						violationMsg = "First Name";
						break;
					case "lastName":
						violationMsg = "Last Name";
						break;
					case "email":
						violationMsg = "E-Mail";
						break;
					case "username":
						violationMsg = "Username";
						break;
					case "password":
						violationMsg = "Password";
						break;
					default:
						violationMsg = "Unknown";
						break propertyIf;
					}
					
					violationMsg += " can not be empty";
				}
				
				violationsStr.append(violationMsg).append("\r\n");
			}
			
			throw new ValidationException(violationsStr.toString());
		}
	}
}
