import java.net.Socket;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.util.ArrayList;

public class UserHandler implements Runnable
{ 
	private Socket socket;
	private String username;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;

	private static ArrayList<UserHandler> userHandlers = new ArrayList<>();

	public UserHandler(Socket socket)
	{
		try{  
			this.socket = socket;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); 
			this.username = bufferedReader.readLine();

			userHandlers.add(this);

			broadcastMessage("Server: " + username + " has entered the chat!");

		} catch(IOException e)
		{
			closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
		}
	}

	@Override 
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
		for(UserHandler user : userHandlers)
		{
			try
			{
				if(!user.username.equals(username))
				{
					user.bufferedWriter.write(message);
					user.bufferedWriter.newLine();
					user.bufferedWriter.flush();
				}

			}catch(IOException e)
			{
				closeEverything(socket, bufferedReader, bufferedWriter);
			}
		}
	}

	public void removeUserHandler()
	{
		userHandlers.remove(this);
		broadcastMessage("Server: " + username + " has left the chat!");
	}


	public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
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
				bufferedReader.close();
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

