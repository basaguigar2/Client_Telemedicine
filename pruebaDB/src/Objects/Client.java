package Objects;


import java.io.Serializable;
import java.util.ArrayList;


public class Client implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7886991344177037244L;
	private Integer id;
	private String name;
	private Doctor doctor;
	private ArrayList <Test> tests;
	private User user;
	
	public Client(Integer id, String name, Doctor doctor, ArrayList<Test> tests) {
		this.id = id;
		this.name = name;
		this.doctor = doctor;
		this.tests = tests;
	}
	
	public Client() {}
	
	public Client (String name, Doctor doctor, User u) {
		this.name=name;
		this.doctor=doctor;
		this.tests= new ArrayList<Test>();
		this.user = u;
	}
	
	public Client (String name) {
		this.name=name;
	}

	public Client(Integer id, String name) {
		this.id = id;
		this.name = name;
		this.tests=new ArrayList<Test>();
	}
	public void addNewTest(Test test) {
		tests.add(test);
	}
	public String getName() {
		return name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", name=" + name + "]";
	}}