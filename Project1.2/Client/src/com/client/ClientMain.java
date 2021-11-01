package com.client;

import java.io.*;
import java.net.*;

public class ClientMain {

	public static void main(String[] args) throws IOException {
		String host;
		int port = Integer.parseInt(args[0]);

		System.out.println("Binding to port " + port);
		host = "localhost";

		try (
				Socket echoSocket = new Socket(host, port);
				PrintWriter out =
						new PrintWriter (echoSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader (
						new InputStreamReader(echoSocket.getInputStream()));
				BufferedReader stdIn =
						new BufferedReader (
								new InputStreamReader(System.in ))
				) {
			String userInput;
			String inputLine;
			
			System.out.println("Type in some text please.");
			while (!(userInput = stdIn.readLine()).equals("exit")) {
				//Client sends output
				out.println(userInput);
				
				//Client gets the server echo
				System.out.println ("Server echo: " + in.readLine ());
				
				//Client gets server output and sends echo
				inputLine = in.readLine();
				System.out.println ("Server Says: " + inputLine);
				out.println("Client at port: " + port + 
						" recieved from server at " + echoSocket.getRemoteSocketAddress() + 
						"  " + inputLine);
				
				//New input
				System.out.println("Type in some text please.");
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
}
