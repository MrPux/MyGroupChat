import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server
{

	ServerSocket serverScoket;

	public static void main(String[] args)
	{
		ServerSocket serverSocket = new ServerSocket(1818);
		Server server = new Server(serverSocket);
		server.start(); 
	}

	public Server(ServerSocket serverSocket)
	{
		this.serverSocket = serverSocket;
 	}

 	public void Start()
 	{
 		try
 		{
 			while(!serverSocet.isClosed())
 			{
 				Socket socket = serverSocket.accept();
 				System.out.println("New User connected! ");

 				UserHandler userHandler = new UserHandler(socket);
 				Thread thread = new Thread(userHander);
 				thread.start();
 			}

 		}catch(IOException e)
 		{
 			closeServer(serverSocket);
 		}
 	}

 	public void closeServer(ServerSocket serverSocket)
 	{
 		try
 		{
 			if(serverSocket != null)
 			{
 				serverSocket.close();
 			}
 		}catch(IOException e)
 	{
 		e.printStackTrace();
 	}
 	}
}
