package se.kth.grupp12.recruitool.model;

public interface AccountDTO {
	public String getFirstName();
	public String getLastName();
	public String getEmail();
	public String getUsername();
	public ApplicationDTO getApplication();
	public Role getRole();
}
