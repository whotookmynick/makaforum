package domainLayer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import searchEngine.CompassSearchEngine;
import searchEngine.Search;
//import searchEngine.SearchImp;
//import searchEngine.SearchTable;

import UI.UIObserver;

import com.thoughtworks.xstream.core.util.Base64Encoder;

import Exceptions.UserAlreadyExistsException;
import Exceptions.UserDoesNotExistException;

//import implementation.ControlerFactory;
import implementation.Message;
import implementation.PersistenceSystemSQL;
import implementation.PersistenceSystemXML;
import implementation.RegisteredUser;

public class TheController {

	protected Collection<RegisteredUser> _loggedUsers;
	protected PersistenceSystem _persistenceLayer;
	protected ConcurrentHashMap<String, Long> _userNameToUserId;
	protected long _currentUserID;
	protected long _currentMsgID;
	protected Search _searchEngine;
	protected Collection<UIObserver> _observersCollection;

	/**
	 * This constructor was created for testing purposes only
	 */
	public TheController(){
		_loggedUsers = new Vector<RegisteredUser>();
//		_persistenceLayer = new PersistenceSystemSQL();
		_persistenceLayer = new PersistenceSystemXML();
		_userNameToUserId = new ConcurrentHashMap<String, Long>();
		_userNameToUserId = _persistenceLayer.createHashTableofUserNametoUID();
		_currentUserID = _persistenceLayer.getCurrentUserID();
		_currentMsgID = _persistenceLayer.getCurrentMsgID();
		/*
		 * Change made by Noam and Dikla to test the new search engine
		 */
		//_searchEngine = new SearchImp((SearchTable)_persistenceLayer);
		_searchEngine = new CompassSearchEngine(this);
		_observersCollection = new Vector<UIObserver>();
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
			updateObservers("message " + msgId);
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
		if (msg != null && _loggedUsers.contains(user) &&
				(user.isModerator() | user.isAdministretor() | user.get_uID() == msg.get_msgPosterID()))
		{
			deleteMessageTree(user, msg);
			return true;
		}
		else{
//			System.out.println("User can't delete message");
			return false;
		}
	}

	private void deleteMessageTree(RegisteredUser user, Message msg) {
		//if (msg.get_fatherMessageID()<0){
			Collection<Message> children = getAllMessagesChildren(msg.get_mID());
			Iterator<Message> iterator = children.iterator();
			while (iterator.hasNext()) {
				Message message = iterator.next();
				deleteMessage(user,message.get_mID());
			}

		//}
		long deletedMid = msg.get_mID();
		_persistenceLayer.deleteMessage(deletedMid);
		updateObservers("delete " + deletedMid);
		_searchEngine.removeMessageFromEngine(msg.get_mID());
		RegisteredUser writer = _persistenceLayer.getUser(msg.get_msgPosterID());
		writer.set_numOfMessages(user.get_numOfMessages()-1);
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
			oldMsg.set_msgBody(newMsgData);
			_searchEngine.insertMessageToEngine(oldMsg);
			updateObservers("edit " + mid);
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
			_searchEngine.insertMessageToEngine(newRepMessage);
			updateObservers("reply " + fatherID);
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
		if ((assigner.isAdministretor() || assigner.isModerator()) && assignee.isMember()){
			assignee.setModerator();
			_persistenceLayer.updateUserType(assignee.get_userName(),1);
			return true;
		}
		else{
			System.out.println("can't assign moderator");
			return false;
		}
	}
	
	public boolean assignAdmin(RegisteredUser assigner,RegisteredUser assignee){
		if (assigner.isAdministretor() && assignee.isMember()){
			assignee.setAdmin();
			_persistenceLayer.updateUserType(assignee.get_userName(),2);
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

	public boolean changePassword(long uid,String newPass)
	{
		String newEcryptedPass = encryptMessage(newPass);
		return _persistenceLayer.changeUserPassword(uid, newEcryptedPass);
	}
	
	/**
	 * Inner method that encrypts the input string using SHA algorithm and then
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

	public void registerObserver(UIObserver newUIObserver)
	{
		_observersCollection.add(newUIObserver);
	}
	
	public void unregisterObserver(UIObserver existingObserver)
	{
		_observersCollection.remove(existingObserver);
	}
	
	private void updateObservers(String updateString)
	{
		Iterator<UIObserver> obsit = _observersCollection.iterator();
		while (obsit.hasNext())
		{
			UIObserver currObs = obsit.next();
			currObs.updateUI(updateString);
		}
	}

	/*From here on these are all functions that serve the graphs
	 */
	
	/**
	 ** This method returns an array of size 30 that holds the
	 ** number of messages sent in the last 30 days by userid
	 ** ans[0] = today - 30
	 **/
	public double[][] getMessagesForThirtyDays(long userid)
	{
		double [][]ans = new double[30][2];
		Calendar now = Calendar.getInstance();
		now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		for (int i = 29; i >= 0; i--)
		{
			ans[i][0] = i;
			ans[i][1] = _persistenceLayer.getNumOfMessageForDay(userid,now.getTimeInMillis());
			now.add(Calendar.SECOND, -86400);
		}
		return ans;
	}
	
	public double[][] getNumOfMessagesPerHour()
	{
		double[][] ans = new double[24][2];
		for (int i = 0; i < 24; i++)
		{
			ans[i][0] = i;
			ans[i][1] = _persistenceLayer.getNumOfMessagesForHour(i);
		}
		return ans;
	}
	
	public double[][] getConnectedUsersPerHour()
	{
		double[][] ans = new double[24][2];
		for (int i = 0; i < 24; i++)
		{
			ans[i][0] = i;
			ans[i][1] = _persistenceLayer.getNumOfUsersForHour(i);
		}
		return ans;		
	}
	
	public void updateHoursOfConnected(int beginHour,int endHour)
	{
		if (endHour > beginHour)
		{
			for (int i = beginHour; i < endHour;i++)
			{
				_persistenceLayer.incNumOfUsersPerHour(i);
			}
		}
		else
		{
			for (int i = beginHour; i < 24; i++)
			{
				_persistenceLayer.incNumOfUsersPerHour(i);
			}
			for (int i = 0; i < endHour; i++)
			{
				_persistenceLayer.incNumOfUsersPerHour(i);
			}
		}
	}
}
