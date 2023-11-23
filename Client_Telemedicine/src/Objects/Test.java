package Objects;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.sql.Date;
public class Test implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1661121873019998142L;
	private Integer id;
	private Date date;
	private Integer frequence;
	private String signal;
	public List <Integer> sequence= new ArrayList<Integer>();
	public List <Integer> value = new ArrayList<Integer>();
	private String column = "Trial\n";
	private Client client;
	
	public Test() {};

	public Test(Integer id, Date date, int frequence, String signal, String column) {
		
		this.setId(id);
		this.date = date;
		this.frequence = frequence;
		this.signal = signal;
		this.column = column;
	}

	
	public Test(Integer frequence, String signal, List<Integer> value) {
		super();
		this.frequence = frequence;
		this.signal = signal;
		this.value = value;
	}

	public String getSignal() {
		return signal;
	}


	public void setSignal(String signal) {
		this.signal = signal;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public int getFrequence() {
		return frequence;
	}


	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}


	public List<Integer> getSeq() {
		return sequence;
	}


	public void setSeq(List<Integer> seq) {
		this.sequence = seq;
	}


	public List<Integer> getValue() {
		return value;
	}


	public void setValue(List<Integer> value) {
		this.value = value;
	}
	
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	public void printList(List<Integer> a,List<Integer> b) {
		for(int i=0;i<a.size();i++) {
		column = column.concat("seq: " + a.get(i)+ " value: " +b.get(i)+ "\n"); 
		}
	}


	@Override
	public String toString() {
		return "Test \n[date=" + date + "\n frequence=" + frequence + "\n signal = " + signal +"\n column=" + column + "]";
	}




	
	
	
}
