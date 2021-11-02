package com.server;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AuthServer {
	private int tryCount = 3;
	private ArrayList<String> userList;
	private boolean isAuth = false;
	private boolean isUsername = true;
	private String username;
	private String password;

	public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean auth) {
		isAuth = auth;
	}

	private void readUserList(ArrayList<String> userList) {
		try {
			File users = new File("./com/server/userList");
			Scanner scan = new Scanner(users);
			while(scan.hasNextLine()) {
				String user = scan.nextLine();
				userList.add(user);
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find the file.");
			e.printStackTrace();
		}
	}

	public boolean userAuth(String username, String password) {
		userList = new ArrayList<>();
		readUserList(userList);
		for(String line : userList) {
			String[] user = line.split(" ");
			if(user[0].equals(username) && user[1].equals(password)) {
				return true;
			}
		}
		userList.clear();
		return false;
	}

	public boolean usernameAuth(String username) {
		userList = new ArrayList<>();
		readUserList(userList);
		for(String line : userList) {
			String[] user = line.split(" ");
			if(user[0].equals(username)) {
				return true;
			}
		}
		userList.clear();
		return false;
	}

	public byte[] createAuthResponse(byte[] msg, String ip, String port) {
		String user = AuthProtocol.byteToPayload(msg);
		if(isUsername) {
			isUsername = !isUsername;
			username = user;
			if(!usernameAuth(username)) {
				return AuthProtocol.messageToByte('0', '2', "User does not exist");
			}
			return AuthProtocol.messageToByte('0', '1', "Enter password: ");
		} else {
			password = user;
			if(userAuth(username, password)) {
				setAuth(true);
				String token = username.substring(0, 3) + "45";
				appendToken(username, ip, port, token);
				return AuthProtocol.messageToByte('0', '3', token);
			} else {
				if (tryCount == 1) {
					return AuthProtocol.messageToByte('0', '2', "User does not exist");
				}
				tryCount--;
				return AuthProtocol.messageToByte('0', '2', "Incorrect password. Try again: ");
			}
		}
	}

	public void appendToken(String username, String ip, String port, String token) {
		try {
			File userList = new File("./com/server/userList");
			BufferedReader reader = new BufferedReader(new FileReader(userList));
			String line = "";
			while ((line = reader.readLine()) != null) {
				String[] lineContent = line.split(" ");
				if (lineContent[0].equals(username)) {
					line = lineContent[0] + " " + lineContent[1] + " " + ip + " " + port + " " + token + "\r\n";
				} 
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("Couldn't find the text file.");
			e.printStackTrace();
		}
	}

	/*public boolean isTokenValid(byte[] msg) {
    	return null;
    }
	 */
}
