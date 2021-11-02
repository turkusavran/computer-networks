package com.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class ConnectionToServer
{
	public static final String DEFAULT_SERVER_ADDRESS = "localhost";
	public static final int DEFAULT_SERVER_PORT = 9999;
	private Socket s;
	Socket fs;
	protected DataInputStream is;
	protected DataOutputStream os;
	protected DataInputStream fis;
	protected DataOutputStream fos;
	protected String serverAddress;
	private AuthClient authClient;
	private byte[] readBuffer;
	protected int serverPort;
	private char phase = '0';

	//@param address IP address of the server, if you are running the server on the same computer as client, put the address as "localhost"
	//@param port port number of the server

	public ConnectionToServer(String address, int port)
	{
		serverAddress = address;
		serverPort = port;
	}

	public void Connect()
	{
		try
		{
			s = new Socket(serverAddress, serverPort);
			is = new DataInputStream(s.getInputStream());
			os = new DataOutputStream(s.getOutputStream());
			authClient = new AuthClient();
			System.out.println("Successfully connected to " + serverAddress + " on port " + serverPort);

		}
		catch (IOException e)
		{
			System.err.println("Error: no server has been found on " + serverAddress + "/" + serverPort);
		}
	}

	//sends the message String to the server and retrieves the answer
	//@param message input message string to the server
	//@return the received server answer
	public String SendForAnswer(String message)
	{
		String response = new String();
		byte[] byteResponse;
		try {
			if(phase == '0') {
				byteResponse = authClient.createCliAuthResponse(message);
				os.write(byteResponse);
				os.flush();

				readBuffer = new byte[100];
				is.read(readBuffer, 0, readBuffer.length);
				response = AuthProtocol.byteToPayload(readBuffer);

				if (authClient.isAuth(readBuffer)) {
					os.write("connected".getBytes());
					os.flush();
					
					readBuffer = new byte[100];
					is.read(readBuffer, 0, readBuffer.length);
					int socket = ByteBuffer.wrap(Arrays.copyOfRange(readBuffer, 0, 4)).getInt();
					fs = new Socket(serverAddress, socket);
					fis = new DataInputStream(fs.getInputStream());
					fos = new DataOutputStream(fs.getOutputStream());
					
					phase = '1';
					response = "Success";
				}
				//TODO: Deal with phase 1
			} else {

			}
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
		}
		return response;
	}

	//Disconnects the socket and closes the buffers
	public void Disconnect()
	{
		try
		{
			is.close();
			os.close();
			s.close();
			System.out.println("ConnectionToServer. SendForAnswer. Connection Closed");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
