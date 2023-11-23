package Objects;


import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 4004074793992980971L;
	private Integer id;
	private String email;
	private byte[] password;
	private Integer role_id;
	private Client cl;

	public User(String email, byte[] password, int role_id) {
		super();
		this.email = email;
		this.password = password;
		this.role_id = role_id;
	}
	
	public User(String email, byte[] password) {
		this.email=email;
		this.password=password;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public Client getCl() {
		return cl;
	}

	public void setCl(Client cl) {
		this.cl = cl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public byte[] getPassword() {
		return password;
	}

	public void setPassword(byte[] password) {
		this.password = password;
	}

	public Integer getRole() {
		return role_id;
	}

	public void setRole(Integer role_id) {
		this.role_id = role_id;
	}

	public User() {
		super();
	}

	public User(int id, Integer role) {
		super();
		this.id = id;
		this.role_id = role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "\nUser:" + "\npassword=" + password + ", \nemail=" + email +  "\nrole: " + role_id;
	}
}