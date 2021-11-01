package com.server;

import java.net.*;
import java.io.*;

public class ServerMain {

	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt(args[0]);

		System.out.println("listening to port " + port);

		try (
				ServerSocket serverSocket = new ServerSocket(port);
				Socket clientSocket = serverSocket.accept();
				PrintWriter out =
						new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				BufferedReader stdIn =
						new BufferedReader(
								new InputStreamReader(System.in))
				) {
			clientSocket.setSoTimeout(15000);
			String inputLine;
			String serverInput = "";

			while (!serverInput.equals("exit")) {
				//Server gets client output and sends echo
				inputLine = in.readLine();
				System.out.println ("Client Says: " + inputLine);
				out.println("Server at port: " + port + 
						" recieved from client at " + clientSocket.getRemoteSocketAddress() + 
						"  " + inputLine);
				
				//Servers gets input and sends to client
				System.out.println("Type in some text please.");
				serverInput = stdIn.readLine();
				out.println(serverInput);
				
				//Server gets client echo
				System.out.println ("Client echo: " + in.readLine ());
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
