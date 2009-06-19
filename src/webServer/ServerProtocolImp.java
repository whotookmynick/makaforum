package webServer;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import searchEngine.Search;

import implementation.Message;
import implementation.MessageDataImp;
import implementation.RegisteredUser;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserDoesNotExistException;
import domainLayer.TheController;

public class ServerProtocolImp implements ServerProtocol {

	TheController _controller;
	RegisteredUser _connectedUser;
	Search _searchEngine;

	public ServerProtocolImp(TheController controller){
		_controller = controller;
		_searchEngine = _controller.get_searchEngine();
	}

	@Override
	public boolean isEnd(String msg) {
		return false;
//		boolean ans;
//		ans = msg.contentEquals("bye");
//		return ans;
	}

	/**
	 * This is the method that implements the basic protocol between the
	 * user and the server. This method receives a request from the client
	 * parses it and returns an answer in form of a string "command args"
	 * IMPORTANT: every response has to end with '\\e'
	 */
	@Override
	public String processMessage(String msg) {
		try {
			String parsedString[] = msg.split(" ");
			if (parsedString[0].contentEquals("login")){
				return userLogIn(parsedString);
			}
			if (parsedString[0].contentEquals("register"))
			{
				return registerNewUser(parsedString);
			}
			if (parsedString[0].contentEquals("message"))
			{
				return addNewMessage(msg, parsedString);
			}
			if (parsedString[0].contentEquals("reply"))
			{
				return replyToMessage(msg, parsedString);
			}
			if (parsedString[0].contentEquals("edit")){
				return editMessage(msg, parsedString);
			}
			if (parsedString[0].contentEquals("display"))
			{
				return displayMessagesOfFather(parsedString);
			}
			if (parsedString[0].contentEquals("logoff"))
			{
				return logoffUser();
			}
			if (parsedString[0].contentEquals("moderator"))
			{
				return giveModeratorPrivellages(parsedString);
			}
			if (parsedString[0].contentEquals("member"))
			{
				return assignMember(parsedString);
			}
			if (parsedString[0].contentEquals("delete"))
			{
				return deleteMessage(parsedString);
			}
			
			if (parsedString[0].contentEquals("search")){
				return searchMethod(msg, parsedString);
			}
			return "print unknown command \\e";
		} catch (Exception e) {
			e.printStackTrace();
			return "server error please try again \\e";
		}
	}

	private String searchMethod(String msg, String[] parsedString) {
		String searchType = parsedString[1];
		if (searchType.contentEquals("author"))
		{
			String authorName = msg.substring(msg.indexOf(searchType) + searchType.length() + 1);
			Collection<Message> allMessages;
			allMessages = _searchEngine.searchByAuthor(authorName);
			String returnString = "print ";
			returnString += createStringFromMessagesCollection(allMessages);
			return returnString + "\\e";
		}
		if (searchType.contentEquals("content"))
		{
			String sentence = msg.substring(msg.indexOf(searchType) + searchType.length() + 1);
			Collection<Message> allMessages;
			allMessages = _searchEngine.searchByContent(sentence);
			String returnString = "print ";
			returnString += createStringFromMessagesCollection(allMessages);
			return returnString + "\\e";
		}
		if (searchType.contentEquals("date"))
		{
			String fromDateString = parsedString[2];
			String toDateString = parsedString[3];
			DateFormat dt = DateFormat.getDateInstance(DateFormat.SHORT);
			try {
				Date fromDate = dt.parse(fromDateString);
				Date toDate = dt.parse(toDateString);
				Collection<Message> allMessages;
				allMessages = _searchEngine.searchByDate(fromDate, toDate);
				String returnString = "print ";
				returnString += createStringFromMessagesCollection(allMessages);
				return returnString + "\\e";
			} catch (ParseException e) {
				return "print date format is incorrect \\e";
			}	
		}
		return "print illegal search type \\e";
	}

	private String deleteMessage(String[] parsedString) {
		long mid = Long.parseLong(parsedString[1]);
		if (_controller.deleteMessage(_connectedUser, mid))
		{
			return "print message deleted \\e";
		}
		else
		{
			return "print you are not authorized to delete message. \\e";
		}
	}

	private String assignMember(String[] parsedString) {
		String userName = parsedString[1];
		RegisteredUser asignee = _controller.getUser(userName);
		if (_controller.assignMember(_connectedUser, asignee))
		{
			return "print user assigned successfully \\e";
		}
		else
		{
			return "print you are not authorized to asign that user \\e";
		}
	}

	private String giveModeratorPrivellages(String[] parsedString) {
		String userName = parsedString[1];
		RegisteredUser assignee = _controller.getUser(userName);
		if (_controller.assignModerator(_connectedUser, assignee))
		{
			return "print user was granted moderator privellages \\e";
		}
		else
		{
			return "print you are not authorized to make that change \\e";
		}
	}

	private String logoffUser() {
		if (_connectedUser == null)
		{
			return "print you are not logged in \\e";
		}
		_controller.logMeOut(_connectedUser);
		_connectedUser = null;
		return "print user logged out successfully \\e";
	}

	/**
	 * Returns the messages to display in the next format:
	 * +1msgid+2msgposter+3content+4posterTime
	 * @param parsedString
	 * @return
	 */
	private String displayMessagesOfFather(String[] parsedString) {
		Collection<Message> allMessages;
		allMessages = _controller.getAllMessagesChildren(Long.parseLong(parsedString[1]));
		String returnString = "print ";
		if (allMessages.isEmpty()){
			returnString += "No messages found";
			return returnString + "  \\e";
		}
		else
		{
			returnString += createStringFromMessagesCollection(allMessages);
			return returnString + " \\e";
		}
	}

	private String editMessage(String msg, String[] parsedString) {
		if (_connectedUser == null)
		{
			return "print you are not logged in \\e";
		}
		String msgId = parsedString[parsedString.length-1];
		String messageContent = msg.substring(parsedString[0].length());
		messageContent = messageContent.substring(0, messageContent.lastIndexOf(msgId));
		if (_controller.editMsg(_connectedUser, Long.parseLong(msgId), new MessageDataImp(messageContent)))
			return "print message edited successfully \\e";
		else
			return "print you are not authorized to edit the message \\e";
	}
	
	private String replyToMessage(String msg, String[] parsedString) {
		if (_connectedUser == null)
		{
			return "print you are not logged in \\e";
		}
		String msgFather = parsedString[parsedString.length-1];
		String messageContent = msg.substring(parsedString[0].length());
		messageContent = messageContent.substring(0, messageContent.lastIndexOf(msgFather));
		_controller.replyToMessage(new MessageDataImp(messageContent), _connectedUser, Long.parseLong(msgFather));
		return "print message added \\e";
	}

	private String addNewMessage(String msg, String[] parsedString) {
		if (_connectedUser == null)
		{
			return "print you are not logged in \\e";
		}
		String messageContent = msg.substring(parsedString[0].length());
		_controller.addNewMessage(new MessageDataImp(messageContent), _connectedUser);
		return "print message added \\e";
	}

	private String registerNewUser(String[] parsedString) {
		try {
			if (_controller.registerNewUser(parsedString[1], parsedString[2]))
			{
				return "print Registration successfull \\e";
			}
		} catch (UserAlreadyExistsException e) {
			return "print unable was not successful \\e";
		}
		return "print unknown command";

	}

	private String userLogIn(String[] parsedString) {
		try {
			_connectedUser = _controller.logMeIn(parsedString[1], parsedString[2]);
			if (_connectedUser != null)
			{
				return "print user logged in succesfully \\e";
			}
			else
			{
				return "print username and/or password are incorrect \\e";
			}
		} catch (UserDoesNotExistException e) {
			return "print username and/or password are incorrect \\e";
		}
	}
	
	private String createStringFromMessagesCollection(Collection<Message> allMessages)
	{
		String returnString = "";
		Iterator<Message> it = allMessages.iterator();
		while (it.hasNext()){
			Message currentMsg = it.next();
			returnString += "+1" + currentMsg.get_mID() + "+2" + currentMsg.get_msgPosterID() + "+3" + currentMsg.get_msgBody().displayMessageData() + "+4" + currentMsg.get_msgPostTime() + "\n";
//			returnString += currentMsg.get_mID() + ": " + currentMsg.get_msgBody().displayMessageData() + "\n";
		}
		return returnString;
	}

}
