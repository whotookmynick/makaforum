package tests;

import java.util.Collection;

import domainLayer.MessageData;
import domainLayer.TheController;
import implementation.Message;
import implementation.MessageDataImp;
import implementation.PersistenceSystemXML;
import implementation.RegisteredUser;
import junit.framework.TestCase;

public class PersistenceSystemTest extends TestCase {
	
	private PersistenceSystemXML _ps;
	
	protected void setUp() throws Exception {
		super.setUp();
		_ps = new PersistenceSystemXML();
	}

	public void testAddMsg() {
		_ps.deleteFileContents("messages.xml");
		MessageData msgData = new MessageDataImp("test message");
		Message msg = new Message(msgData,1,1);
		_ps.addMsg(msg);
		Message msgFromDb = _ps.getMessage(1);
		assertEquals(msg.get_mID(), msgFromDb.get_mID());
	}

	public void testAddUser() {
		_ps.deleteFileContents("users.xml");
		RegisteredUser newUser = new RegisteredUser("user1",1);
		_ps.addUser(newUser, "pass1");
		RegisteredUser userFromDb = _ps.getUser(1);
		assertEquals(newUser.get_uID(),userFromDb.get_uID());
		
	}

	public void testChangeUserPassword() {
		_ps.deleteFileContents("users.xml");
		_ps.deleteFileContents("passes.xml");
		String newUserPass = "pass2";
		RegisteredUser newUser = new RegisteredUser("user1",1);
		_ps.addUser(newUser, "pass1");
		_ps.changeUserPassword(newUser.get_uID(), newUserPass);
		String newUserPassFromDB = _ps.getUserPassword(newUser.get_uID());
		assertTrue(newUserPass.contentEquals(newUserPassFromDB));
		
	}

	public void testDeleteMessage() {
		_ps.deleteFileContents("messages.xml");		
		MessageData msgData = new MessageDataImp("test message");
		Message msg = new Message(msgData,1,1);
		_ps.addMsg(msg);
		_ps.deleteMessage(1);
		Message msgFromDb = _ps.getMessage(1);
		assertTrue(msgFromDb == null);
	}


	public void testEditMessage() {
		String newMessageContent = "new test message content";
		_ps.deleteFileContents("messages.xml");
		MessageData msgData = new MessageDataImp("test message");
		Message msg = new Message(msgData,1,1);
		_ps.addMsg(msg);
		_ps.editMessage(1, new MessageDataImp(newMessageContent));
		Message msgFromDb = _ps.getMessage(1);
		assertTrue(msgFromDb.get_msgBody().toString().contentEquals(newMessageContent));
	}

	public void testGetMessage() {
		_ps.deleteFileContents("messages.xml");
		MessageData msgData = new MessageDataImp("test message");
		Message msg = new Message(msgData,1,1);
		_ps.addMsg(msg);
		Message msgFromDb = _ps.getMessage(1);
		assertEquals(msg.get_mID(), msgFromDb.get_mID());
	}

	public void testGetUser() {
		_ps.deleteFileContents("users.xml");
		RegisteredUser newUser = new RegisteredUser("user1",1);
		_ps.addUser(newUser, "pass1");
		RegisteredUser userFromDb = _ps.getUser(1);
		assertEquals(newUser.get_uID(),userFromDb.get_uID());
	}

	public void testGetUserPassword() {
		_ps.deleteFileContents("users.xml");
		_ps.deleteFileContents("passes.xml");
		RegisteredUser newUser = new RegisteredUser("user1",1);
		_ps.addUser(newUser, "pass1");
		String newUserPassFromDB = _ps.getUserPassword(newUser.get_uID());
		assertTrue(newUserPassFromDB.contentEquals("pass1"));
	}

	public void testGetMessagesWithFather() {
		_ps.deleteFileContents("messages.xml");
		MessageData msgData = new MessageDataImp("test message");
		Message msg1 = new Message(msgData,1,1);
		Message msg2 = new Message(msgData,1,1,2);
		Message msg3 = new Message(msgData,1,1,3);
		_ps.addMsg(msg1);
		_ps.addMsg(msg2);
		_ps.addMsg(msg3);
		Collection<Message> allMsgs = _ps.getMessagesWithFather(1);
		assertTrue(allMsgs.size() == 2);
	}

}
