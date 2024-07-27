
import java.net.Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

import java.util.Scanner;

// Variable Declarations
public class User{

private Socket socket;
private String username;
private BufferedReader bufferedReader;
private BufferedWriter bufferedWriter;

public static void main(String[] args) throws IOException
{ 
		Scanner input = new Scanner(System.in);
		System.out.println("What will your user name be: ");
		String username = input.nextLine();
		Socket socket = new Socket("localhost", 1818);

		User user = new User(username, socket); 

		user.listenForMessage();
		user.sendMessage();


		input.close(); 
}

// Initialazing Phase

public User(String username, Socket socket)
{
	try
	{
		this.username = username;
		this.socket = socket;
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

	}catch(IOException e)
	{
		closeEverything(socket, bufferedReader, bufferedWriter);
	}
}

public void sendMessage()
{
	try
	{
		bufferedWriter.write(username);
		bufferedWriter.newLine();
		bufferedWriter.flush();

		Scanner input = new Scanner(System.in);
		while(socket.isConnected())
		{
			String message = input.nextLine();
			bufferedWriter.write(username + ": " + message);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} 
		input.close();

	}catch(IOException e)
	{
		closeEverything(socket, bufferedReader, bufferedWriter);
	}
}


public void listenForMessage()
{
	new Thread ( new Runnable()
	{
		@Override
		public void run()
		{
			String message;
			try
			{
				while(socket.isConnected())
				{
					message = bufferedReader.readLine();
					System.out.println(message);

				}
			}catch(IOException e)
			{
				closeEverything(socket, bufferedReader, bufferedWriter);
			}
		}
	}).start();
}
//

public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter)
{
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