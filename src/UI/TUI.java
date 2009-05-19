package UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Messages that the clienty can send:
 * login userName password //login to system
 * register username password //register a new username
 * message username messagedata messageisreplytomessage //write a new message
 * display fatherid
 * @author Noam
 *
 */
public class TUI implements Runnable{
	
	private Socket _clientSocket; // the connection socket
	private PrintWriter _socketWriter;
	private BufferedReader _socketReader;
	private BufferedReader _userReader;
	
	@Override
	public void run() {
		String host = "localhost";
		int port = 1234;
		connectSocketsToServer(host, port);
		
		String msg;
		_userReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.println("Waiting for user input");
			while ((msg = _userReader.readLine()) != null)
			{
				_socketWriter.println(msg);
				String repliedMessage = _socketReader.readLine();
				String displayString = repliedMessage.substring(6);
				System.out.println(displayString);
				System.out.println("Waiting for user input");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			closeConnetion();
		}

		
	}
	
	private void connectSocketsToServer(String host,int port){
		try {
			_clientSocket = new Socket(host, port); // host and port
	  		_socketWriter = new PrintWriter(_clientSocket.getOutputStream(), true);
		} catch (UnknownHostException e) {
	  		System.exit(1);
	    } catch (IOException e) {
			System.exit(1);
		}
	
		// Initialize an input stream
		try {
			_socketReader = new BufferedReader(new InputStreamReader(_clientSocket.getInputStream()));
		} catch (IOException e) {
			System.exit(1);
		}

	}

	private void closeConnetion(){
	// Close all I/O
		try {
			_socketWriter.close();
			_socketReader.close();
			_userReader.close();
			_clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		System.out.println("Starting MAKAForum Client");
		Thread client = new Thread(new TUI());
		client.start();
	}
}