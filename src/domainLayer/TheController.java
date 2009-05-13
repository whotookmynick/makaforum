package domainLayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import com.thoughtworks.xstream.core.util.Base64Encoder;

import Exceptions.UserAlreadyExistsException;
import Exceptions.UserDoesNotExistException;

import implementation.Message;
import implementation.NonRegisteredUser;
import implementation.PersistenceSystemXML;
import implementation.RegisteredUser;
import implementation.UserPassword;

public class TheController {
	
	protected Collection<RegisteredUser> _loggedUsers;
	protected PersistenceSystem _persistenceLayer;
	protected Hashtable<String, Long> _userNameToUserId;
	protected long _currentUserID;
	
	/**
	 * This constructor was created for testing purposes only
	 */
	public TheController(){
		_loggedUsers = new Vector<RegisteredUser>();
		_persistenceLayer = new PersistenceSystemXML();
		_userNameToUserId = new Hashtable<String, Long>();
		long uid = 1;
		_userNameToUserId.put("user1",uid);
		_currentUserID = 1;
	}
	
	public boolean logMeIn(String userName,String pass) throws UserDoesNotExistException{
		RegisteredUser currentUser;
		Long userId = _userNameToUserId.get(userName);
		currentUser = _persistenceLayer.getUser(userId);
		String encryptedPass = _persistenceLayer.getUserPassword(userId);
		if (currentUser != null){
			String passToTest = encryptMessage(pass);
			if (encryptedPass.contentEquals(passToTest))
			{
				_loggedUsers.add(currentUser);
				System.out.println("User " + currentUser.get_userName() + " is logged in");
				return true;
			}
			else
			{
				return false;// user was not authenticated.
			}
		}
		System.out.println("Username or password do not exist");
		throw new UserDoesNotExistException();
				
	}
	
	/**
	 * Registers a new user and encrypts it's password using SHA algorithm
	 * which is built in in java.
	 * @param userName
	 * @param password
	 * @return
	 * @throws UserAlreadyExistsException
	 */
	public boolean registerNewUser(String userName,String password) throws UserAlreadyExistsException{
		if (!_userNameToUserId.containsKey(userName)){
			RegisteredUser theNewUser = new RegisteredUser(userName,_currentUserID);
			_currentUserID++;
			String encryptedPass = encryptMessage(password);
			if (encryptedPass != null){
				_persistenceLayer.addUser(theNewUser,encryptedPass);
				_loggedUsers.add(theNewUser);
				return true;
			}
			else 
			{
				System.out.println("Password was not encrypted");
			}
		}
		else
		{
			System.out.println("The username is not available");
			throw new UserAlreadyExistsException();
		}
		return false;
		
	}
	
	public void addNewMessage(RegisteredUser user,Message msg){
		if (msg != null && _loggedUsers.contains(user)){
			_persistenceLayer.addMsg(msg);
			user.set_numOfMessages(user.get_numOfMessages()+1);
		}
	}
	
	public void editMsg(RegisteredUser user,long mid,MessageData newMsgData){
		Message oldMsg = _persistenceLayer.getMessage(mid);
		if (oldMsg.get_msgPosterID() == user.get_uID()){
			_persistenceLayer.editMessage(mid, newMsgData);
		}
		System.out.println("Message does not belong to user");
	}

	public Collection<Message> getAllMessagesChildren(long fatherID){
		return _persistenceLayer.getMessagesWithFather(fatherID);
	}

	public void replyToMessage(MessageData replyMsgData,long msgPoster,long fatherID,long originalID){
		Message newRepMessage = new Message(replyMsgData,msgPoster,fatherID,originalID);
		_persistenceLayer.addMsg(newRepMessage);
	}
	
	public Collection<RegisteredUser> get_userContainer() {
		return _loggedUsers;
	}

	public void set_userContainer(Collection<RegisteredUser> container) {
		_loggedUsers = container;
	}
	
	private static String encryptMessage(String msg){
		MessageDigest md;
		String encryptedMsg = null;
		try {
			md = MessageDigest.getInstance("MD5");
			Base64Encoder be = new Base64Encoder();
			encryptedMsg = new String(be.encode(md.digest(msg.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encryptedMsg;
	}
}
