package webServer;

import java.util.Collection;
import java.util.Iterator;

import implementation.Message;
import implementation.MessageDataImp;
import implementation.RegisteredUser;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserDoesNotExistException;
import domainLayer.TheController;

public class ServerProtocolImp implements ServerProtocol {

	TheController _controller;
	RegisteredUser _connectedUser;

	public ServerProtocolImp(TheController controller){
		_controller = controller;
	}

	@Override
	public boolean isEnd(String msg) {
		return false;
//		boolean ans;
//		ans = msg.contentEquals("bye");
//		return ans;
	}

	/**
	 * NOT YET IMPLEMENTED
	 */
	@Override
	public String processMessage(String msg) {
		String parsedString[] = msg.split(" ");
		if (parsedString[0].contentEquals("login")){
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
		if (parsedString[0].contentEquals("register"))
		{
			try {
				if (_controller.registerNewUser(parsedString[1], parsedString[2]))
				{
					return "print Registration successfull \\e";
				}
			} catch (UserAlreadyExistsException e) {
				return "print unable was not successful \\e";
			}
		}
		if (parsedString[0].contentEquals("message"))
		{
			if (_connectedUser == null)
			{
				return "print you are not logged in \\e";
			}
			String messageContent = msg.substring(parsedString[0].length());
			_controller.addNewMessage(new MessageDataImp(messageContent), _connectedUser);
			return "print message added \\e";
		}
		if (parsedString[0].contentEquals("reply"))
		{
			if (_connectedUser == null)
			{
				return "print you are not logged in \\e";
			}
			String msgFather = parsedString[parsedString.length-1];
			String messageContent = msg.substring(parsedString[0].length());
			messageContent.substring(0, messageContent.indexOf(msgFather));
			_controller.replyToMessage(new MessageDataImp(messageContent), _connectedUser, Long.parseLong(msgFather));
			return "print message added \\e";
		}
		
		if (parsedString[0].contentEquals("display"))
		{
			Collection<Message> allMessages;
			allMessages = _controller.getAllMessagesChildren(Long.parseLong(parsedString[1]));
			String returnString = "print ";
			if (allMessages.isEmpty()){
				returnString += "No messages found";
				return returnString + "  \\e";
			}
			else
			{
				Iterator<Message> it = allMessages.iterator();
				while (it.hasNext()){
					Message currentMsg = it.next();
					returnString += currentMsg.get_mID() + ": " + currentMsg.get_msgBody().displayMessageData() + "\n";
				}
				return returnString + " \\e";
			}
		}
		if (parsedString[0].contentEquals("logoff"))
		{
			if (_connectedUser == null)
			{
				return "print you are not logged in \\e";
			}
			_controller.logMeOut(_connectedUser);
			_connectedUser = null;
			return "print user logged out successfully \\e";
		}
		return "print unknown command \\e";
	}

}
