package communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ClosingHandler implements Runnable {
	private boolean serverRunning = true;
	private Socket socket;

	// Excepcion scanner
	@Override
	public void run() {
		Scanner console = new Scanner(System.in);
		try {
			while (serverRunning) {
				int closer;
				closer = console.nextInt();
				System.out.println(closer);
				if (closer == 0) {
					serverRunning = false;
					if (Server_Object_Threads.runningThreads == 0) {
						Server_Object_Threads.closer();
					} else {
						System.out.println(
								"There are clients connected to the server. Are you sure that you want to close the server? \n Type 1 to close, type 2 to cancel.");
						closer = console.nextInt();
						System.out.println(closer);
						if (closer != 1 && closer != 2) {
							System.out.println("That is not a valid option.");
							closer = console.nextInt();
						} else if (closer == 1) {
							Server_Object_Threads.closer();
							try {
								setSocket(new Socket("localhost", 9030));
								console.close();
							} catch (UnknownHostException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (closer == 2) {
							serverRunning = true;
						}
					}
				}
			}
		} catch (InputMismatchException ex) {
			System.out.println("You should write a number");
			Server_Object_Threads.start_ClosingHandler();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}