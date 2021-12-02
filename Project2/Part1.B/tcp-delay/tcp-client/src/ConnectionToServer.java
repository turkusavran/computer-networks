import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Yahya Hassanzadeh on 20/09/2017.
 */

public class ConnectionToServer
{
	public static final String DEFAULT_SERVER_ADDRESS = "localhost";
	public static final int DEFAULT_SERVER_PORT = 64145;
	private Socket s;
	protected BufferedReader is;
	protected PrintWriter os;
	protected long inTime;
	protected long outTime;
	protected String serverAddress;
	protected int serverPort;

	/**
	 *
	 * @param address IP address of the server, if you are running the server on the same computer as client, put the address as "localhost"
	 * @param port port number of the server
	 */
	public ConnectionToServer(String address, int port)
	{
		serverAddress = address;
		serverPort = port;
	}

	/**
	 * Establishes a socket connection to the server that is identified by the serverAddress and the serverPort
	 */
	public void Connect()
	{
		try
		{
			s = new Socket(serverAddress, serverPort);
			is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			os = new PrintWriter(s.getOutputStream());

		}
		catch (IOException e)
		{
			System.err.println("Error: no server has been found on " + serverAddress + "/" + serverPort);
		}
	}

	/**
	 * sends the message String to the server and retrives the answer
	 * @param message input message string to the server
	 * @return the received server answer
	 */
	public String SendForAnswer(char message)
	{
		String response = new String();
		try
		{
			if(message == '1' || message == '2'){
				inTime = System.nanoTime();
				os.write(message);
				os.flush();
			}
			
			response = is.readLine();
			outTime = System.nanoTime();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("ConnectionToServer. SendForAnswer. Socket read Error");
		}
		return response;
	}


	/**
	 * Disconnects the socket and closes the buffers
	 */
	public void Disconnect()
	{
		try
		{
			is.close();
			os.close();
			s.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
