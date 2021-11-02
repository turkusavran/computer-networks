package com.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

class ServerThread extends Thread {
	protected DataInputStream is;
	protected DataOutputStream os;
	protected Socket s;
	protected DataInputStream fis;
	protected DataOutputStream fos;

	protected Socket fs;
	private String line = new String();
	private byte[] readBuffer;
	private byte[] serverResponse;
	private AuthServer authServer;
	private String clientMessage;

	// Creates a server thread on the input socket
	public ServerThread(Socket s) {
		this.s = s;
	}

	// The server thread, echos the client until it receives the exit string from the client
	public void run() {
		try {
			authServer = new AuthServer();
			clientMessage = new String();
			serverResponse = new byte[1000000];

			is = new DataInputStream(s.getInputStream());
			os = new DataOutputStream(s.getOutputStream());   
		} catch (IOException e) {
			System.err.println("Server Thread. Run. IO error in server thread");
		}

		try {
			while (!AuthProtocol.byteToPayload(serverResponse).equalsIgnoreCase("User does not exist")
					&& !clientMessage.equals("exit")) {
				readBuffer = new byte[300];
				is.read(readBuffer, 0, readBuffer.length);

				SocketAddress clientSocketAdress = s.getRemoteSocketAddress();
				char phase = AuthProtocol.byteToPhase(readBuffer);

				if(phase == '0') {
					serverResponse = authServer.createAuthResponse(readBuffer, clientSocketAdress.toString().split(":")[0],
							clientSocketAdress.toString().split(":")[1]);
					os.write(serverResponse);
					os.flush();

					if(authServer.isAuth()) {
						readBuffer = new byte[300];
						is.read(readBuffer, 0, readBuffer.length);
						if (new String(Arrays.copyOfRange(readBuffer, 0, 9)).equals("connected")) {
							int dataPort = 5555;
							try {
								ServerSocket serverSocket = new ServerSocket(dataPort);
								System.out.println("Oppened up a data socket on " + Inet4Address.getLocalHost());
								os.write(ByteBuffer.allocate(4).putInt(dataPort).array());
								while (fs == null) {
									ListenAndAccept(serverSocket);
								}

								//TODO: deal with api

							} catch (IOException e) {
								System.err.println("Error on oppening a server socket");
								e.printStackTrace();
							}
						}

					} 

					//TODO: deal with phase '1'
				} else {


				}
			}
		} catch (IOException e) {
			line = this.getName(); //reused String line for getting thread name
			System.err.println("Server Thread. Run. IO Error/ Client " + line + " terminated abruptly");
		} catch (NullPointerException e) {
			line = this.getName(); //reused String line for getting thread name
			System.err.println("Server Thread. Run.Client " + line + " Closed");
		} finally {
			try {
				System.out.println("Closing the connection");
				if (is != null) {
					is.close();
					System.err.println(" Socket Input Stream Closed");
				}

				if (os != null) {
					os.close();
					System.err.println("Socket Out Closed");
				}
				if (s != null) {
					s.close();
					System.err.println("Socket Closed");
				}
			}
			catch (IOException ie) {
				System.err.println("Socket Close Error");
			}
		}
	}

	private void ListenAndAccept(ServerSocket socket) {
		try {
			fs = socket.accept();
			fis = new DataInputStream(fs.getInputStream());
			fos = new DataOutputStream(fs.getOutputStream());
			System.out.println("A connection was established with a client on the address of "
					+ fs.getRemoteSocketAddress());
			socket.close();
		} catch (Exception e) {
			System.err.println("Connection error");
			e.printStackTrace();
		}
	}
}
