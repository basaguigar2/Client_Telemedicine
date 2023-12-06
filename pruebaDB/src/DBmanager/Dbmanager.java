
package DBmanager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import Objects.Client;
import Objects.Doctor;
import Objects.Test;
import Objects.User;

public class Dbmanager {
	private Connection c;

	public void connect() {

		try {
			// Open database connection
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:./db/Test6_DB.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			System.out.println("Database connection opened.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Error, database exception.");
		} catch (Exception e) {
			System.out.println("Error, couldn't connect to data based.");
			e.printStackTrace();
		}
	}

	public void create2() {
		Statement stmt1;
		try {
			// Create tables: begin
			stmt1 = c.createStatement();
			String sql1 = "CREATE TABLE doctor " + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT NOT NULL,"
					+ "user_id INTEGER REFERENCES users(id))";
			stmt1.executeUpdate(sql1);
			sql1 = "CREATE TABLE client " + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + "name TEXT NOT NULL,"
					+ "doctor_id INTEGER REFERENCES doctor(id)," + "user_id INTEGER REFERENCES users(id))";
			stmt1.executeUpdate(sql1);
			sql1 = "CREATE TABLE test " + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + "date DATE NOT NULL,"
					+ "signal TEXT NOT NULL," + "frequence INTEGER NOT NULL," + "column TEXT NOT NULL,"
					+ "client_id INTEGER REFERENCES client(id))";
			stmt1.executeUpdate(sql1);
			sql1 = "CREATE TABLE users " + "(id INTEGER PRIMARY KEY AUTOINCREMENT," + "email TEXT NOT NULL,"
					+ "password BYTES NOT NULL," + "role_id INTEGER NOT NULL)";
			stmt1.executeUpdate(sql1);
			stmt1.close();
			System.out.println("Tables created.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void disconnect() {
		try {
			// Close database connection
			c.close();
			System.out.println("Database connection close");
		} catch (SQLException e) {
			System.out.println("There was a problem while closing the database connection.");
			e.printStackTrace();
		}
	}

	public void addPerson(Client cl) {
		PreparedStatement prep;
		Doctor doc = cl.getDoctor();
		User u = cl.getUser();
		try {
			String sql = "INSERT INTO client (name, doctor_id, user_id) " + "VALUES (?,?,?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, cl.getName());
			prep.setInt(2, doc.getId());
			prep.setInt(3, u.getId());
			prep.executeUpdate();
			System.out.println("Patient info processed");
			System.out.println("Records inserted. \n \n");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void addDoctor(Doctor d) { // Hay que modificar para cuando se haga el doctor
		PreparedStatement prep;
		User u = d.getUser();
		try {
			String sql = "INSERT INTO doctor (name,user_id) " + "VALUES (?,?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, d.getName());
			prep.setInt(2, u.getId());
			prep.executeUpdate();
			System.out.println("Doctor info processed");
			System.out.println("Records inserted.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Doctor searchDoctorbyId(int id) {
		Doctor doc = null;
		String sql = "SELECT * FROM doctor WHERE id LIKE ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int c = rs.getInt("id");
				String doctor_name = rs.getString("name");
				doc = new Doctor(c, doctor_name);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}

	public void addTest(Test t) {
		PreparedStatement prep;
		Client pat = t.getClient();
		try {

			String sql1 = "INSERT INTO test (date, signal, frequence, column, client_id) " + "VALUES (?,?,?,?,?)";
			prep = c.prepareStatement(sql1);
			prep.setDate(1, t.getDate());
			prep.setString(2, t.getSignal());
			prep.setInt(3, t.getFrequence());
			prep.setString(4, t.getColumn());
			prep.setInt(5, pat.getId());
			prep.executeUpdate();
			prep.close();
			System.out.println("Test info processed");
			System.out.println("Records inserted. \n \n");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public Client searchPatientById(int n) {
		Client cl = null;
		String sql = "SELECT * FROM client WHERE id LIKE ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, n);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String client_name = rs.getString("name");
				cl = new Client(id, client_name);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cl;
	}

	public Client searchPatientByName(String name) {
		Client cl = null;
		String sql = "SELECT * FROM client WHERE name LIKE ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, name);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String client_name = rs.getString("name");
				cl = new Client(id, client_name);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cl;
	}

	public void test_patient(String nombre_cliente, Test test) {
		Client c = searchPatientByName(nombre_cliente);
		if (test != null) {
			c.addNewTest(test);
		}
		test.setClient(c);
		addTest(test);
	}

	public String checkPasswordDoc(String e, byte[] p) {
		User u = null;
		int role_id = 0;
		try {
			String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND role_id = 2";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, e);
			prep.setBytes(2, p);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				String email = rs.getString("email");
				byte[] password = rs.getBytes("password");
				role_id = rs.getInt("role_id");
				u = new User(email, password, role_id);
			}
			rs.close();
			prep.close();
			if (u != null) {
				if (role_id == 2) {
					return "ok d";
				}
			} else {
				return "The user does not exist";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public String checkPasswordCL(String e, byte[] p) {
		User u = null;
		int role_id = 0;
		try {
			String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND role_id = 1";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, e);
			prep.setBytes(2, p);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				String email = rs.getString("email");
				byte[] password = rs.getBytes("password");
				role_id = rs.getInt("role_id");
				u = new User(email, password, role_id);
			}
			rs.close();
			prep.close();
			if (u != null) {
				if (role_id == 1) {
					return "ok c";
				} else if (role_id == 2) {
					return "The user does not exist";
				}
			} else {
				return "The user does not exist";
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void newUser(User u) {

		PreparedStatement prep;

		try {
			String sql = "INSERT INTO users (email, password, role_id)" + "VALUES (?,?,?)";
			prep = c.prepareStatement(sql);
			prep.setString(1, u.getEmail());
			prep.setBytes(2, u.getPassword());
			prep.setInt(3, u.getRole());
			prep.executeUpdate();
			System.out.println("User info processed");
			System.out.println("Records inserted. \n \n");

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Integer> viewDoctors() {
		int id = 0;
		ArrayList<Integer> list = new ArrayList<>();
		String sql = "SELECT id FROM doctor";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
				list.add(id);
			}
			rs.close();
			prep.close();

			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int selectRandomId(ArrayList<Integer> arrayList) {
		int numero = 0;
		Random random = new Random();
		if (arrayList.size() != 0) {
			numero = random.nextInt(arrayList.size());
			return arrayList.get(numero);
		} else {
			return -1;
		}
	}

	public int searchIdUser(String e) {

		int id = -1;
		String sql = "SELECT * FROM users WHERE email LIKE ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, e);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
			rs.close();
			prep.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return id;
	}

	public int searchDoctorbyUserId(int user_id) {
		int id = 0;
		String sql = "SELECT * FROM doctor WHERE user_id LIKE ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, user_id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public int searchPatientbyUserId(int user_id) {
		int id = -1;
		String sql = "SELECT * FROM client WHERE user_id LIKE ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, user_id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				id = rs.getInt("id");
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public ArrayList<Test> viewAllTest(int clientId) {
		ArrayList<Test> list = new ArrayList<Test>();
		Test te = null;
		String sql = "SELECT * FROM test WHERE client_id LIKE ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, clientId);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				Date d = rs.getDate("date");
				String signal = rs.getString("signal");
				int f = rs.getInt("frequence");
				String column = rs.getString("column");
				te = new Test(id, d, f, signal, column);
				list.add(te);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Client> viewAllPatient(int doctorId) {
		ArrayList<Client> list = new ArrayList<Client>();
		Client cli = null;
		String sql = "SELECT * FROM client WHERE doctor_id LIKE ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, doctorId);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				cli = new Client(id, name);
				list.add(cli);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String searchTestbyId(int id) {

		String t = "";
		Test te = null;
		String sql = "SELECT * FROM test WHERE id LIKE ?";
		try {
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int i = rs.getInt("id");
				Date d = rs.getDate("date");
				String signal = rs.getString("signal");
				int f = rs.getInt("frequence");
				String column = rs.getString("column");
				te = new Test(i, d, f, signal, column);
				t = te.toString();
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

}
