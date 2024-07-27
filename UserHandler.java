import java.net.Socket;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutPutStreamReader;

import java.util.ArrayList;

public class UserHandler implements Runnable
{ 
	private Socket socket;
	private String username;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;

	private static ArrayList<UserHandler> userHandler = new ArrayList<>()

	public UserHandler(Socket socket)
	{
		try 
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStreamReader()));
			this.bufferedWriter = new BufferedWriter(new BufferedWriter(socket.getOutputStreamWriter())); 
			this.username = bufferedReader.readLine();

			userHandler.add(this);

			broadcastMessage("Server: " + username + "has entered the chat!");

		} catch(IOException e)
		{
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	

@override 
public void run()
{
	String activeUserMessageListener;
	while(socket.isConnected())
	{
		try
		{
			activeUserMessageListener = bufferedReader.readLine();
			broadcastMessage(activeUserMessageListener);

		}catch(IOException e)
		{
			closeEverything(socket, bufferedReader, bufferedWriter);
		}

	}
} 


// ---------------------------------------------------------

	public void broadcastMessage(String message)
	{
		for(UserHandler user : userHandler)
		{
			try
			{
				if(!user.username.equals(username))
				{
					System.out.pritnln(message);
				}

			}catch(IOException e)
			{
				closeEverything(socket, bufferedReader, bufferedWriter);
			}
		}
	}

	public void removeUserHandler()
	{
		userHandler.remove(this);
		broadcastMessaga("Server: " + username + " has left the chat!");
	}


	public void closeEverything(Socket socket, BufferedReder bufferedReader, BufferedWriter bufferedWriter)
	{
		removeUserHandler();
		try
		{
			if(socket != null)
			{
				socket.close();
			}
			if(bufferedReader != null)
			{
				bufferedReder.close();
			}
			if(bufferedWriter != null)
			{
				bufferedWriter.close();
			}

		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

