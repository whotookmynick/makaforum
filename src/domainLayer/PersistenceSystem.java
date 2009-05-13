package domainLayer;

import java.util.Collection;

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
	
	public boolean deleteUser(long uid);
	
	public void addMsg(Message msg);
	
	public Message getMessage(long mID);
	
	public void editMessage(long mID,MessageData newMsg);
	
	public Message deleteMessage(long mid);
	
	public Collection<Message> getMessagesWithFather(long fatherID);
}
