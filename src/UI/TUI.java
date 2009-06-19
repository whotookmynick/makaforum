package UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Commands that the client can send:
 * login userName password //login to system
 * register username password //register a new username
 * message messagedata //write a new message
 * reply message_content(string) msgFatherId
 * edit new content(string) msgID // edits the message msgID to the new content
 * display fatherid  //displays all the messages that the father is fatherid
 * moderator userName //give moderator privellages to userName
 * member userName // turn userName into regular member
 * delete msgId  //deletes the message with id msgId
 * search content content to search for // Searches for messages with the content specified
 * search author authorName // searches for messages with the specified author
 * search date fromdate todate // searches for messages written between these dates.
 * 							   // IMPORTANT: date must be in format of M/D/YYYY
 * logoff // log user off
 * 
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
				String displayString = sendMessageAndWaitForReply(msg);
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

	/**
	 * This is the function that needs to be called from the GUI.
	 * @param msg
	 * @return
	 * @throws IOException
	 */
	public String sendMessageAndWaitForReply(String msg) throws IOException {
		_socketWriter.println(msg);
		String repliedMessage = "";
		while (repliedMessage.indexOf("\\e") < 0)
			repliedMessage += _socketReader.readLine() + "\n";
		repliedMessage = repliedMessage.substring(0,repliedMessage.length()-3);
		//System.out.println(repliedMessage);
		String displayString = repliedMessage.substring("print".length()+1);
		return displayString;
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
