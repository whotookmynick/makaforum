package domainLayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import searchEngine.Search;
import searchEngine.SearchImp;
import searchEngine.SearchTable;

import com.thoughtworks.xstream.core.util.Base64Encoder;

import Exceptions.UserAlreadyExistsException;
import Exceptions.UserDoesNotExistException;

import implementation.Message;
import implementation.PersistenceSystemXML;
import implementation.RegisteredUser;

public class TheController {

	protected Collection<RegisteredUser> _loggedUsers;
	protected PersistenceSystem _persistenceLayer;
	protected ConcurrentHashMap<String, Long> _userNameToUserId;
	protected long _currentUserID;
	protected long _currentMsgID;
	protected Search _searchEngine;

	/**
	 * This constructor was created for testing purposes only
	 */
	public TheController(){
		_loggedUsers = new Vector<RegisteredUser>();
		_persistenceLayer = new PersistenceSystemXML();
		_userNameToUserId = new ConcurrentHashMap<String, Long>();
		_userNameToUserId = _persistenceLayer.createHashTableofUserNametoUID();
		_currentUserID = _persistenceLayer.getCurrentUserID();
		_currentMsgID = _persistenceLayer.getCurrentMsgID();
		_searchEngine = new SearchImp((SearchTable)_persistenceLayer);
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
	public RegisteredUser logMeIn(String userName,String pass) throws UserDoesNotExistException{
		RegisteredUser currentUser;
		Long userId = _userNameToUserId.get(userName);
		if (userId == null)
			return null;
		currentUser = _persistenceLayer.getUser(userId);
		String encryptedPass = _persistenceLayer.getUserPassword(userId);
		if (currentUser != null){
			String passToTest = encryptMessage(pass);
			if (encryptedPass.contentEquals(passToTest))
			{
				synchronized (_loggedUsers)
				{
					_loggedUsers.add(currentUser);
				}
				System.out.println("User " + currentUser.get_userName() + " is logged in");
				return currentUser;
			}
			else
			{
				return null;// user was not authenticated.
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
		synchronized(_loggedUsers)
		{
			_loggedUsers.remove(ru);
		}
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
			_userNameToUserId.put(userName,_currentUserID);
			_currentUserID++;
			_persistenceLayer.incUserId();
			String encryptedPass = encryptMessage(password);
			if (encryptedPass != null){
				_persistenceLayer.addUser(theNewUser,encryptedPass);
				synchronized(_loggedUsers)
				{
					_loggedUsers.add(theNewUser);
				}
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
	 * @param msgData
	 * @param user
	 * @param originalID
	 */
	public void addNewMessage(MessageData msgData,RegisteredUser user){
		if (user == null)
			return;
		if (user.isMember() && _loggedUsers.contains(user)){
			//Added by Noam
			long msgId = _persistenceLayer.getCurrentMsgID();
			Message newMessage = new Message(msgData,user.get_uID(),msgId);
			_persistenceLayer.incMsgId();
			//Message newMessage = new Message(msgData,user.get_uID(),originalID);
			_persistenceLayer.addMsg(newMessage);
			user.set_numOfMessages(user.get_numOfMessages()+1);
			_searchEngine.insertMessageToEngine(newMessage);
		}
		else{
			System.out.println("User can't add message");
		}
	}

	/**
	 * This method takes care of deleting a message form the system and the persistence system.
	 * @author Roee
	 * @param user
	 * @param msg
	 */
	public boolean deleteMessage(RegisteredUser user,long mID){
		Message msg = _persistenceLayer.getMessage(mID);
		if (user.isModerator() && msg != null && _loggedUsers.contains(user)){
			if (msg.get_fatherMessageID()<0){
				Collection<Message> children = getAllMessagesChildren(msg.get_mID());
				Iterator<Message> iterator = children.iterator();
				while (iterator.hasNext()) {
					Message message = iterator.next();
					deleteMessage(user,message.get_mID());
				}

			}
			_persistenceLayer.deleteMessage(msg.get_mID());
			RegisteredUser writer = _persistenceLayer.getUser(msg.get_msgPosterID());
			writer.set_numOfMessages(user.get_numOfMessages()-1);
			return true;
		}
		else{
			System.out.println("User can't add message");
			return false;
		}
	}



	/**
	 * This method checks that the given user is authorized to edit the message
	 * and then replaces the old message data with the new one.
	 * @param user
	 * @param mid
	 * @param newMsgData
	 */
	public boolean editMsg(RegisteredUser user,long mid,MessageData newMsgData){
		Message oldMsg = _persistenceLayer.getMessage(mid);
		if ((user.isModerator() || oldMsg.get_msgPosterID() == user.get_uID()) && _loggedUsers.contains(user)){
			_persistenceLayer.editMessage(mid, newMsgData);
			return true;
		}
		else{
			System.out.println("Message does not belong to user");
			return false;
		}
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
	 * @param user
	 * @param fatherID
	 * @param originalID
	 */
	public void replyToMessage(MessageData replyMsgData,RegisteredUser user,long fatherID){
		if (user.isMember() && _loggedUsers.contains(user)){
			long msgId = _persistenceLayer.getCurrentMsgID();
			Message newRepMessage = new Message(replyMsgData,user.get_uID(),fatherID,msgId);
			_persistenceLayer.incMsgId();
			_persistenceLayer.addMsg(newRepMessage);
			user.set_numOfMessages(user.get_numOfMessages()+1);
		}
		else{
			System.out.println("User can't reply to message");
		}
	}

	/**
	 *
	 * @author Moti and Roee
	 */
	public boolean assignModerator(RegisteredUser assigner,RegisteredUser assignee){
		if (assigner.isAdministretor() && assignee.isMember()){
			assignee.setModerator();
			return true;
		}
		else{
			System.out.println("can't assign moderator");
			return false;
		}
	}

	/**
	 *
	 * @author Moti and Roee
	 */
	public boolean assignMember(RegisteredUser assigner,RegisteredUser assignee){
		if (assigner.isAdministretor() && assignee.isMember()){
			assignee.setMember();
			return true;
		}
		else{
			System.out.println("can't assign member");
			return false;
		}
	}

	public RegisteredUser getUser(String userName){
		if (!_userNameToUserId.containsKey(userName))
			return null;
		long uid = _userNameToUserId.get(userName);
		return _persistenceLayer.getUser(uid);
	}

	public Collection<RegisteredUser> get_userContainer() {
		return _loggedUsers;
	}

	public void set_userContainer(Collection<RegisteredUser> container) {
		_loggedUsers = container;
	}

	public Search get_searchEngine() {
		return _searchEngine;
	}

	public long get_uidFromUserName(String userName)
	{
		Long uid =_userNameToUserId.get(userName);
		if (uid == null)
			return -1;
		else
			return uid;
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
