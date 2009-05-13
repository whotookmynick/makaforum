//package domainLayer;
//
//import java.util.Collection;
//import java.util.Hashtable;
//
//import Exceptions.UserAlreadyExistsException;
//import Exceptions.UserDoesNotExistException;
//
//import implementation.Message;
//import implementation.NonRegisteredUser;
//import implementation.RegisteredUser;
//
//public class Controller {
//	
//	protected Collection<RegisteredUser> _loggedUsers;
//	protected PersistenceSystem _persistenceLayer;
//	protected Hashtable<String, Long> _userNameToUserId;
//	protected long _currentUserID;
//	
//	public boolean logMeIn(String userName,String pass) throws UserDoesNotExistException{
//		RegisteredUser currentUser;
//		Long userId = _userNameToUserId.get(userName);
//		currentUser = _persistenceLayer.getUser(userId);
//		if (currentUser != null){
//			if (currentUser.get_password().contentEquals(pass))
//			{
//				_loggedUsers.add(currentUser);
//				return true;
//			}
//		}
//		System.out.println("Username or password do not exist");
//		throw new UserDoesNotExistException();
//				
//	}
//	
//	public boolean registerNewUser(String userName,String password) throws UserAlreadyExistsException{
//		if (!_userNameToUserId.containsKey(userName)){
//			RegisteredUser theNewUser = new RegisteredUser(userName,password,_currentUserID);
//			_currentUserID++;
//			_persistenceLayer.addUser(theNewUser);
//			_loggedUsers.add(theNewUser);
//		}
//		System.out.println("The username is not available");
//		throw new UserAlreadyExistsException();
//		
//	}
//	
//	public void addNewMessage(RegisteredUser user,Message msg){
//		if (msg != null){
//			_persistenceLayer.addMsg(msg);
//			user.set_numOfMessages(user.get_numOfMessages()+1);
//		}
//	}
//	
//	public void editMsg(RegisteredUser user,long mid,MessageData newMsgData){
//		Message oldMsg = _persistenceLayer.getMessage(mid);
//		if (oldMsg.get_msgPosterID() == user.get_uID()){
//			_persistenceLayer.editMessage(mid, newMsgData);
//		}
//		System.out.println("Message does not belong to user");
//	}
//
//	public Collection<Message> getAllMessagesChildren(long fatherID){
//		return _persistenceLayer.getMessagesWithFather(fatherID);
//	}
//
//	public void replyToMessage(MessageData replyMsgData,long msgPoster,long fatherID,long mid){
//		Message newRepMessage = new Message(replyMsgData,msgPoster,fatherID,mid);
//	}
//	
//	public Collection<RegisteredUser> get_userContainer() {
//		return _loggedUsers;
//	}
//
//	public void set_userContainer(Collection<RegisteredUser> container) {
//		_loggedUsers = container;
//	}
//	
//	
//}
