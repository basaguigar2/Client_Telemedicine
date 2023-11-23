package Objects;

import java.io.Serializable;
import java.util.List;

public class Doctor implements Serializable{
	 /**
	 * 
	 */
	private static final long serialVersionUID = -4608964793631710889L;
	
	private Integer id;
	private String name;
	private List <Client> clients;
	private User user;
	
	
	public Doctor() {}
	
	public Doctor(String name) {
		super();
		this.name = name;
	}

	public Doctor(Integer id, String name, List<Client> clients) {
		
		this.id = id;
		this.name = name;
		this.clients = clients;
	}
	
	public void add_client (Client c) {
		clients.add(c);
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Client> getClients() {
		return clients;
	}
	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	 
}
