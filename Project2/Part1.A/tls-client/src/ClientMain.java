/**
 * Copyright [2017] [Yahya Hassanzadeh-Nazarabadi]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
public class ClientMain
{
	public final static String TLS_SERVER_ADDRESS = "localhost";
	public final static int TLS_SERVER_PORT = 64145;

	public static void main(String[] args)
	{
		/*
        Creates an SSLConnectToServer object on the specified server address and port
		 */
		SSLConnectToServer sslConnectToServer = new SSLConnectToServer(TLS_SERVER_ADDRESS, TLS_SERVER_PORT);
		/*
        Connects to and then disconnects from the server
		 */
		for (int digit = 0; digit < 5; digit++) {
			sslConnectToServer.Connect();
			System.out.println(sslConnectToServer.SendForAnswer(null));
			sslConnectToServer.Disconnect();  
		}
		// Print the merged ID
		System.out.println(sslConnectToServer.myID);
	}
}
