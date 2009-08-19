package domainLayer;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import implementation.Message;
import implementation.RegisteredUser;

/**
 * This interface defines the needs from the persistence system.
 * @author aradno
 *
 */
public interface PersistenceSystem {
	
	public void addUser(RegisteredUser ud,String password);
	
	public RegisteredUser getUser(long uid);
	
	public String getUserPassword(long uid);
	
	public boolean changeUserPassword(long uid,String newPass);
	
	/* no need */
	public boolean deleteUser(String userName);
	
	public boolean deletePassword(long uid);
	
	public void addMsg(Message msg);
	
	public Message getMessage(long mID);
	
	public void editMessage(long mID,MessageData newMsg);
	
	public Message deleteMessage(long mid);
	
	public Collection<Message> getMessagesWithFather(long fatherID);
	
	public ConcurrentHashMap<String, Long> createHashTableofUserNametoUID();
	
	public int getCurrentUserID();
	
	public int getCurrentMsgID();
	
	public void incMsgId();
	
	public void incUserId();
	
	public void changeUserName(String oldName, String newName);
}
