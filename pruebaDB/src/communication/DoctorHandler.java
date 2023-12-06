package communication;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import DBmanager.Dbmanager;

public class DoctorHandler implements Runnable{

	final Dbmanager dbMana;
	final Socket s;
	final InputStream iS;
	final OutputStream oS;
	final DataInputStream diS;
	final DataOutputStream doS;
	final BufferedReader bR;
	final PrintWriter pW;
	final ObjectOutputStream OoS;
	
	public DoctorHandler(Dbmanager dbMana, Socket s, InputStream iS, OutputStream oS, DataInputStream diS,
			DataOutputStream doS,BufferedReader bR, PrintWriter pW, ObjectOutputStream ooS) {
		this.dbMana = dbMana;
		this.s = s;
		this.iS = iS;
		this.oS = oS;
		this.diS = diS;
		this.doS = doS;
		this.bR = bR;
		this.pW = pW;
		this.OoS = ooS;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Server_Object_Threads.doctorElection(dbMana, s, iS, oS, diS, doS,bR,pW,OoS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		Server_Object_Threads.runningThreads--;
	}
}
