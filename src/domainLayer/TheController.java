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
	
	/**
	 * This method loggs the user to the system allowing that his password matches
	 * the one specified in the persistence system.
	 * NOTE: this method assumes that there is a way to convert username to userID
	 * @param userName
	 * @param pass
	 * @return
	 * @throws UserDoesNotExistException
	 */
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
	 * This method receive a registered user, checks if he is logged in
	 * and if so loggs him out of the system. Furthermore the system also updates
	 * any information about the user that needs to be updated.
	 * @param ru
	 * @return
	 */
	public boolean logMeOut(RegisteredUser ru){
		_loggedUsers.remove(ru);
		return true;
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
	
	/**
	 * This method takes care of adding a message to the system and the persistence system.
	 * @param user
	 * @param msg
	 */
	public void addNewMessage(RegisteredUser user,Message msg){
		if (msg != null && _loggedUsers.contains(user)){
			_persistenceLayer.addMsg(msg);
			user.set_numOfMessages(user.get_numOfMessages()+1);
		}
	}
	
	/**
	 * This method checks that the given user is authorized to edit the message
	 * and then replaces the old message data with the new one.
	 * @param user
	 * @param mid
	 * @param newMsgData
	 */
	public void editMsg(RegisteredUser user,long mid,MessageData newMsgData){
		Message oldMsg = _persistenceLayer.getMessage(mid);
		if (oldMsg.get_msgPosterID() == user.get_uID()){
			_persistenceLayer.editMessage(mid, newMsgData);
		}
		System.out.println("Message does not belong to user");
	}

	/**
	 * is not implemented right now
	 * @param fatherID
	 * @return
	 */
	public Collection<Message> getAllMessagesChildren(long fatherID){
		return _persistenceLayer.getMessagesWithFather(fatherID);
	}

	/**
	 * Rights a new message to the persistence system where the father message is fatherID
	 * @param replyMsgData
	 * @param msgPoster
	 * @param fatherID
	 * @param originalID
	 */
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
	
	/**
	 * Inner message that encrypts the input string using SHA algorithm and then
	 * also Base64 in order to make sure it can be re read with XML.
	 * @param msg
	 * @return
	 */
	private static String encryptMessage(String msg){
		MessageDigest md;
		String encryptedMsg = null;
		try {
			md = MessageDigest.getInstance("SHA");
			Base64Encoder be = new Base64Encoder();
			encryptedMsg = new String(be.encode(md.digest(msg.getBytes())));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encryptedMsg;
	}
}
