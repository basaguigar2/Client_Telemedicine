package communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Objects.Test;
import Objects.User;

public class Client_Object {

	OutputStream outputStream = null;
	DataOutputStream dataoutput = null;
	Socket socket = null;
	DataInputStream datainput = null;
	PrintWriter printWriter = null;
	InputStream input = null;

	public void connection_client() {
		try {
			socket = new Socket("localhost", 9004);
			outputStream = socket.getOutputStream();
			dataoutput = new DataOutputStream(outputStream);
			input = socket.getInputStream();
			datainput = new DataInputStream(input);
			printWriter = new PrintWriter(socket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send_election(int byteSend) {
		try {
			outputStream.write(byteSend);
			outputStream.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String send_registerCL(User u) {
		// Send email, password, role, name of the client and id of his doctor
		// These data will be stored in their corresponding tables
		String m = null;
		try {
			
			byte [] byteArray = create_hash(u.getPassword());
			dataoutput.writeInt(byteArray.length);
			int character = 0;
			while (character < byteArray.length-1) {
				outputStream.write(byteArray[character]);
				outputStream.flush();
				character++;
			}
			outputStream.write('x');
			outputStream.flush();
			dataoutput.writeInt(u.getRole());
			printWriter.println(u.getEmail());
			m = datainput.readUTF();
		} catch (IOException ex) {
			System.out.println("It was not possible to connect to the server.");
		}
		return m;
	}

	public String send_login(String e, String p) {
		// Send email and password looking for a confirmation if the data is correct
		try {
			byte [] byteArray = create_hash(p);
			dataoutput.writeInt(byteArray.length);
			int character = 0;
			while (character < byteArray.length-1) {
				outputStream.write(byteArray[character]);
				outputStream.flush();
				character++;
			}
			outputStream.write('x');
			outputStream.flush();
			printWriter.println(e);
			String line = datainput.readUTF();
			return line;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return "The data is not correct";
		}

	}

	public int search_client(String email) {
		// Send the name of a client looking for the id
		try {
			printWriter.println(email);
			int id = datainput.readInt();
			return id;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public void send_test(String name, Test t) {
		// Send a new test to be stored in the database
		try {
			dataoutput.writeInt(t.getFrequence());
			printWriter.println(name);
			printWriter.println(t.getSignal());
			System.out.println("Test terminado");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void send_column(Test t) {
		try {
			int character;
			int i = 0;
			character = t.getColumn().charAt(i);
			while (i < t.getColumn().length()) {
				outputStream.write(character);
				outputStream.flush();
				character = t.getColumn().charAt(i++);
			}
			outputStream.write('x');
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Test> receive_test_List(int id) {
		ArrayList<Test> receivedList = new ArrayList<>();
		try {
			dataoutput.writeInt(id);
			int listSize = datainput.readInt();
			for (int i = 0; i < listSize; i++) {
				Test receivedTest = receiveTestObject();
				receivedList.add(receivedTest);
			}
		} catch (IOException e) {
			System.out.println("Socket cerrado");
			e.printStackTrace();
		}
		return receivedList;

	}

	public Test receiveTestObject() {
		Test receivedTest = null;
		try {
			long dateMillis = datainput.readLong();
			Date date = new Date(dateMillis);
			int id = datainput.readInt();
			int frequence = datainput.readInt();
			String signal = datainput.readUTF();
			String column = datainput.readUTF();
			receivedTest = new Test(id, date, frequence, signal, column);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return receivedTest;
	}

	public void releaseResources() {
		// Release all the resources required for the class
		try {
			dataoutput.close();
			printWriter.close();
			datainput.close();
		} catch (IOException ex) {
			Logger.getLogger(Client_Object.class.getName()).log(Level.SEVERE, null, ex);
		}
		try {
			socket.close();
		} catch (IOException ex) {
			Logger.getLogger(Client_Object.class.getName()).log(Level.SEVERE, null, ex);
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

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
