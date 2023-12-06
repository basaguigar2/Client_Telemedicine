package communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

import DBmanager.Dbmanager;
import Objects.Client;
import Objects.Doctor;
import Objects.Test;
import Objects.User;

public class Server_Object_Threads {

	private static Dbmanager dbMana = new Dbmanager();
	public static ServerSocket sS = null;
	private static Socket s = null;
	public static Test t = new Test();
	public static int runningThreads = 0;

	private static boolean bucle = false;
	public static boolean sC = false;

	public static void main(String[] args) {

		// Initiate the server waiting a connection
		try {
			sS = new ServerSocket(9018);
			dbMana.connect();
			//dbMana.create2();
			start_ClosingHandler();
			while (!sC) {
				s = sS.accept();
				System.out.println("Conexion establecida");
				IdentifierHandler iH = new IdentifierHandler(s);
				new Thread(iH).start();
				runningThreads++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Cerramos servidor");
			System.exit(-1);
		} finally {
			releaseResourcesServer();
		}

	}

	public static void start_ClosingHandler() {
		ClosingHandler cH = new ClosingHandler();
		new Thread(cH).start();
	}

	public static void loginElection(Dbmanager dbMana, Socket s, InputStream iS, OutputStream oS, DataInputStream diS,
			DataOutputStream doS, BufferedReader bR, PrintWriter pW, ObjectOutputStream OoS) throws IOException {
		int byteRead = -1;
		bucle = false;
		while (!bucle) {
			try {
				byteRead = iS.read();
				if (byteRead == -1 || byteRead == 'i') {
					releaseResources(s, bR, pW, iS, oS, diS, doS);
				} else {
					switch (byteRead) {
					case 'a':
						receiveRegister(diS, bR, doS, iS);
						break;
					case 'b':
						confirmationLoginCL(iS, oS, diS, doS, bR, pW, OoS);
						break;
					case 'h':
						confirmationLoginDoc(iS, oS, diS, doS, bR, pW, OoS);
						break;
					}
				}
			} catch (SocketException ex) {
				releaseResources(s, bR, pW, iS, oS, diS, doS);
				bucle = true;
			}
		}
	}

	public static void clientElection(Dbmanager dbMana, Socket s, InputStream iS, OutputStream oS, DataInputStream diS,
			DataOutputStream doS, BufferedReader bR, PrintWriter pW, ObjectOutputStream OoS) throws IOException {
		int byteRead = -1;
		boolean client_loop = false;
		while (!client_loop) {
			try {
				byteRead = iS.read();
				if (byteRead == -1 || byteRead == 'i') {
					client_loop = true;
					releaseResources(s, bR, pW, iS, oS, diS, doS);
				} else {
					switch (byteRead) {
					case 'c':
						searchIDClient(bR, doS);
						break;
					case 'f':
						newTest(diS, bR);
						break;
					case 'g':
						receiveColumn(iS);
						break;
					case 'j':
						listTest(diS, doS, OoS, doS);
						break;
					}
				}
			} catch (SocketException ex) {
				releaseResources(s, bR, pW, iS, oS, diS, doS);
			}
		}
	}

	public static void doctorElection(Dbmanager dbMana, Socket s, InputStream iS, OutputStream oS, DataInputStream diS,
			DataOutputStream doS, BufferedReader bR, PrintWriter pW, ObjectOutputStream OoS) throws IOException {
		int byteRead = -1;
		boolean doctor_loop = false;
		while (!doctor_loop) {
			try {
				byteRead = iS.read();
				if (byteRead == -1 || byteRead == 'i') {
					doctor_loop = true;
					releaseResources(s, bR, pW, iS, oS, diS, doS);
				} else {
					switch (byteRead) {
					case 'd':
						listClient(diS, oS, OoS, doS);
						break;
					case 'j':
						listTest(diS, doS, OoS, doS);
						break;
					case 'p':
						searchIDDoctor(bR, doS);
						break;
					}
				}
			} catch (SocketException ex) {
				releaseResources(s, bR, pW, iS, oS, diS, doS);
			}
		}
	}

	public static void receiveRegister(DataInputStream diS, BufferedReader bR, DataOutputStream doS, InputStream iS) {
		try {
			int byteRead;
			int i = 0;
			int length = diS.readInt();
			byte[] byteArray = new byte[length];
			while ((byteRead = iS.read()) != 'x') {
				byteArray[i] = (byte) byteRead;
			}
			int role = diS.readInt();
			String email = bR.readLine();
			int check = dbMana.searchIdUser(email);
			if (check == -1) {
				User u = new User(email, byteArray, role);
				dbMana.newUser(u);
				int user_id = dbMana.searchIdUser(email);
				u.setId(user_id);
				if (role == 1) {
					int id = dbMana.selectRandomId(dbMana.viewDoctors());
					if (id != -1) {
						Doctor d = dbMana.searchDoctorbyId(id);
						Client cl = new Client(email, d, u);
						dbMana.addPerson(cl);
						doS.writeUTF("ok");
					} else {
						doS.writeUTF("No doctors");
					}
				} else if (role == 2) {
					Doctor doc = new Doctor(email, u);
					dbMana.addDoctor(doc);
					doS.writeUTF("ok");
				}
				
			} else {
				doS.writeUTF("Username already registrated");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error recepcion Datos");
			e.printStackTrace();
		}
	}

	public static void confirmationLoginDoc(InputStream iS, OutputStream oS, DataInputStream diS, DataOutputStream doS,
			BufferedReader bR, PrintWriter pW, ObjectOutputStream OoS) {// recibe email y pasword y devuelve string con
		// verificacion
		try {

			int byteRead;
			int i = 0;
			int length = diS.readInt();
			byte[] byteArray = new byte[length];
			while ((byteRead = iS.read()) != 'x') {
				byteArray[i] = (byte) byteRead;
			}
			String email = bR.readLine();
			String respuesta = dbMana.checkPasswordDoc(email, byteArray);
			doS.writeUTF(respuesta);
			if (respuesta == "ok d") {
				bucle = true;
				runningThreads++;
				DoctorHandler iH = new DoctorHandler(dbMana, s, iS, oS, diS, doS, bR, pW, OoS);
				new Thread(iH).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error recepcion Datos");
			e.printStackTrace();
		}
	}

	public static void confirmationLoginCL(InputStream iS, OutputStream oS, DataInputStream diS, DataOutputStream doS,
			BufferedReader bR, PrintWriter pW, ObjectOutputStream OoS) {// recibe email y pasword y devuelve string con
		// verificacion
		try {
			int byteRead;
			int i = 0;
			int length = diS.readInt();
			byte[] byteArray = new byte[length];
			while ((byteRead = iS.read()) != 'x') {
				byteArray[i] = (byte) byteRead;
			}
			String email = bR.readLine();
			String respuesta = dbMana.checkPasswordCL(email, byteArray);
			doS.writeUTF(respuesta);
			if (respuesta == "ok c") {
				bucle = true;
				runningThreads++;
				ClientHandler iH = new ClientHandler(dbMana, s, iS, oS, diS, doS, bR, pW, OoS);
				new Thread(iH).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error recepcion Datos");
			e.printStackTrace();
		}
	}

	public static void searchIDClient(BufferedReader bR, DataOutputStream doS) {
		try {
			String email = bR.readLine();
			int id_user = dbMana.searchIdUser(email);
			int id = dbMana.searchPatientbyUserId(id_user);
			doS.writeInt(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error recepcion Datos");
			e.printStackTrace();
		}
	}
	
	public static void searchIDDoctor(BufferedReader bR, DataOutputStream doS) {
		try {
			String email = bR.readLine();
			int id_user = dbMana.searchIdUser(email);
			int id = dbMana.searchDoctorbyUserId(id_user);
			doS.writeInt(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error recepcion Datos");
			e.printStackTrace();
		}
	}

	public static void listTest(DataInputStream diS, OutputStream oS, ObjectOutputStream OoS, DataOutputStream doS) {
		try {
			int n = diS.readInt();
			Client c = dbMana.searchPatientById(n);
			int id = c.getId();
			ArrayList<Test> list = dbMana.viewAllTest(id);
			doS.writeInt(list.size());
			for (Test test : list) {
				sendTestObject(test, doS);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error recepcion Datos");
			e.printStackTrace();
		}
	}

	public static void sendTestObject(Test testObject, DataOutputStream doS) {
		try {
			doS.writeLong(testObject.getDate().getTime());
			doS.writeInt(testObject.getId());
			doS.writeInt(testObject.getFrequence());
			doS.writeUTF(testObject.getSignal());
			doS.writeUTF(testObject.getColumn());
			doS.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void listClient(DataInputStream diS, OutputStream oS, ObjectOutputStream OoS, DataOutputStream doS) {
		try {
			int n = diS.readInt();
			Doctor d = dbMana.searchDoctorbyId(n);
			int id = d.getId();
			ArrayList<Client> list = dbMana.viewAllPatient(id);
			doS.writeInt(list.size());
			for (Client cl : list) {
				sendClientObject(cl, doS);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error recepcion Datos");
			e.printStackTrace();
		}
	}

	public static void sendClientObject(Client cl, DataOutputStream doS) {
		try {
			doS.writeInt(cl.getId());
			doS.writeUTF(cl.getName());
			doS.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void newTest(DataInputStream diS, BufferedReader bR) {
		try {
			int frequence = diS.readInt();
			String name = bR.readLine();
			String signal = bR.readLine();
			Date date = Date.valueOf(LocalDate.now());
			t.setDate(date);
			t.setFrequence(frequence);
			t.setSignal(signal);
			dbMana.test_patient(name, t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error recepcion Datos");
			e.printStackTrace();
		}
	}

	private static void receiveColumn(InputStream iS) {
		int byteRead;
		String column = "";
		try {
			while ((byteRead = iS.read()) != 'x') {
				char caracter = (char) byteRead;
				column = column + caracter;
			}
			t.setColumn(column);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closer() throws IOException {
		sC = true;
		sS.close();
	}

	
	public static void releaseResources(Socket s, BufferedReader bR, PrintWriter pW, InputStream iS, OutputStream oS,
			DataInputStream diS, DataOutputStream doS) {
		try {
			bR.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		pW.close();
		try {
			iS.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try {
			oS.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try {
			diS.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try {
			doS.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		try {
			s.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void releaseResourcesServer() {
		try {
			sS.close();
			dbMana.disconnect();
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] create_hash(String m) {
		MessageDigest md;
		try {
			System.out.println(m);
			md = MessageDigest.getInstance("MD5");
			md.update(m.getBytes());
			byte[] hash = md.digest();
			return hash;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String recorrer_list(ArrayList<String> list) {
		String t = "List: ";
		for (int i = 0; i < list.size(); i++) {
			t = t + "\n " + list.get(i);
		}
		return t;
	}
}
