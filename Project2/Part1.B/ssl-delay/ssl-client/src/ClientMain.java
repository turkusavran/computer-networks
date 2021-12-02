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

		SSLConnectToServer sslConnectToServer = new SSLConnectToServer(TLS_SERVER_ADDRESS, TLS_SERVER_PORT);

		// Message 1
		sslConnectToServer.Connect();
		System.out.println(sslConnectToServer.SendForAnswer('1'));
		System.out.printf("Start: %d, finish: %d, delay: %d nanoseconds\n", sslConnectToServer.inTime, sslConnectToServer.outTime, sslConnectToServer.outTime-sslConnectToServer.inTime);
		sslConnectToServer.Disconnect();

		// Message 2
		sslConnectToServer.Connect();
		System.out.println(sslConnectToServer.SendForAnswer('2'));
		sslConnectToServer.Disconnect();

		for(int i = 0; i < 23; i++){
			sslConnectToServer.Connect();
			System.out.println(sslConnectToServer.SendForAnswer(' '));

			if(i == 22){
				System.out.printf("Start: %d, finish: %d, delay: %d nanoseconds\n", sslConnectToServer.inTime, sslConnectToServer.outTime, sslConnectToServer.outTime-sslConnectToServer.inTime);
			}
			sslConnectToServer.Disconnect(); 
		}
	}
}
