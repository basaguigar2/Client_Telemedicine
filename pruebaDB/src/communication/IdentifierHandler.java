package communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import DBmanager.Dbmanager;

public class IdentifierHandler implements Runnable{

	private static Dbmanager dbMana;
	private static InputStream iS;
	private static OutputStream oS;
	private static DataInputStream diS;
	private static DataOutputStream doS;
	private static BufferedReader bR;
	private static PrintWriter pW;
	private static ObjectOutputStream OoS;
	Socket s;
	
	public IdentifierHandler(Socket s) {
		this.s = s;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			iS = s.getInputStream();
			oS = s.getOutputStream();
			diS = new DataInputStream(iS);
			doS = new DataOutputStream(oS);
			bR = new BufferedReader(new InputStreamReader(iS));
			pW = new PrintWriter(oS);
			Server_Object_Threads.loginElection(dbMana, s, iS, oS, diS, doS,bR,pW, OoS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Server_Object_Threads.runningThreads--;
	}

}