package UI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

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
 * user newUserName
 * password newPassword
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
	
	public void connectSocketsToServer(String host,int port){
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
	
	/**
	 * This method receives a string of format
	 * +1msgid+2msgposter+3content+4posterTime
	 * and returns it clean to display
	 * @param displayString
	 * @return
	 */
	private String formatDisplayString(String recvdString) {
		String displayString = "";
		String []seperated = recvdString.split("\n");
		for (int i = 0; i < seperated.length-1; i++)
		{
			String currMsg = seperated[i];
			String []parsedMsg = parseIncomingReceivedMessages(currMsg);
			displayString += "Message id: " + parsedMsg[0] +"\n";
			displayString += "Posted by: " + parsedMsg[1] + "\n";
			displayString += "Posted At: " + parsedMsg[3] + "\n";
			displayString += "Content: " + parsedMsg[2] + "\n";
		}
		return displayString;
	}

	/**
	 * This function receives a String in the format
	 * +1msgid+2msgposter+3content+4posterTime
	 * and retuns an array of Strings where each place in the array
	 * holds a different value. In the same order of the original String
	 * @param currMsg
	 * @return
	 * result[0] = msgID
	 * result[1] = msgPosterID
	 * result[2] = content
	 * result[3] = postTime
	 */
	public static String[] parseIncomingReceivedMessages(String currMsg)
	{
		String []res = new String[4];
		String currMsgID = currMsg.substring(currMsg.indexOf("+1")+2,currMsg.indexOf("+2"));
		String currMsgPosterID = currMsg.substring(currMsg.indexOf("+2")+2,currMsg.indexOf("+3"));
		String currMsgData = currMsg.substring(currMsg.indexOf("+3")+2,currMsg.indexOf("+4"));
		String currMsgTime = currMsg.substring(currMsg.indexOf("+4")+2);
		Date currMsgDate = new Date(Long.parseLong(currMsgTime));
		res[0] = currMsgID;
		res[1] = currMsgPosterID;
		res[2] = currMsgData;
		res[3] = currMsgDate.toString();
		return res;
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
