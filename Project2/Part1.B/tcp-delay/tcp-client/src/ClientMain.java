public class ClientMain {

	public static void main(String[] args) {
		ConnectionToServer connectionToServer = new ConnectionToServer(ConnectionToServer.DEFAULT_SERVER_ADDRESS, ConnectionToServer.DEFAULT_SERVER_PORT);
        connectionToServer.Connect();
        
        // Send the message
        System.out.println(connectionToServer.SendForAnswer('1'));
        System.out.printf("Start: %d, finish: %d, delay: %d nanoseconds\n", connectionToServer.inTime, connectionToServer.outTime, connectionToServer.outTime-connectionToServer.inTime);
        
        // Disconnect and connect for non-persistent 
        connectionToServer.Disconnect();
        connectionToServer.Connect();
        
        // Send the message
        System.out.println(connectionToServer.SendForAnswer('2'));
        
        connectionToServer.Disconnect();
        
        for(int i = 0; i < 23; i++){
            connectionToServer.Connect();
            System.out.println(connectionToServer.SendForAnswer(' '));
            if(i == 22){
                System.out.printf("Start: %d, finish: %d, delay: %d nanoseconds\n", connectionToServer.inTime, connectionToServer.outTime, connectionToServer.outTime-connectionToServer.inTime);
            }
            connectionToServer.Disconnect(); 
        }
	}

}
