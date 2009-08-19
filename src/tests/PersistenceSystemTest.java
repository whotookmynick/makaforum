package tests;

import java.util.Collection;

import domainLayer.MessageData;
import domainLayer.PersistenceSystem;
import domainLayer.TheController;
import implementation.Message;
import implementation.MessageDataImp;
import implementation.PersistenceSystemSQL;
import implementation.PersistenceSystemXML;
import implementation.RegisteredUser;
import junit.framework.TestCase;
/**
 * checks data base for correct behavior in tests.
 * leaves the DB without extra information.
 * @author ohad and eldar
 *
 */
public class PersistenceSystemTest extends TestCase {
	
	private PersistenceSystem _ps;
	private static int _i = 1;
	
	protected void setUp() throws Exception {
		super.setUp();
		_ps = new PersistenceSystemSQL();
	}

	public void testAddMsg() {
		//_ps.deleteFileContents("messages.xml");
		MessageData msgData = new MessageDataImp("test message");
		Message msg = new Message(msgData,1,_ps.getCurrentMsgID());
		_ps.addMsg(msg);
		Message msgFromDb = _ps.getMessage(_ps.getCurrentMsgID() - 1);
		assertEquals(msg.get_mID(), msgFromDb.get_mID());//checks msg id equality
		
		//delete the message  we have added.
		//if we want to see the message on the data base comment next line.
		_ps.deleteMessage(_ps.getCurrentMsgID() - 1);
	}

	public void testAddUser() {
		//_ps.deleteFileContents("users.xml");
		//_ps.deleteUser("user1");
		
		//create a new user with correct id.
		RegisteredUser newUser = new RegisteredUser("userTest1",_ps.getCurrentUserID());
		_ps.addUser(newUser, "pass1");
		//System.out.println(_ps.getCurrentUserID());
		RegisteredUser userFromDb = _ps.getUser(_ps.getCurrentUserID() - 1);
		assertEquals(newUser.get_uID(),userFromDb.get_uID());
		
		//delete the user password.
		System.out.println(_ps.getCurrentUserID() - 1);
		_ps.deletePassword(_ps.getCurrentUserID() - 1);
		_ps.deleteUser("userTest1");
		
	}

	public void testChangeUserPassword() {
		//_ps.deleteFileContents("users.xml");
		//_ps.deleteFileContents("passes.xml");
		//_ps.deleteUser("user1");
		String newUserPass = "pass2";
		RegisteredUser newUser = new RegisteredUser("userTest1",_ps.getCurrentUserID());
		_ps.addUser(newUser, "pass1");
		_ps.changeUserPassword(newUser.get_uID(), newUserPass);
		String newUserPassFromDB = _ps.getUserPassword(newUser.get_uID());
		assertTrue(newUserPass.contentEquals(newUserPassFromDB));
		//no need to keep the password so I delete it.
		System.out.println(_ps.getCurrentUserID() - 1);
		_ps.deletePassword(_ps.getCurrentUserID() - 1);
		_ps.deleteUser("userTest1");
	}

	public void testDeleteMessage() {
		//_ps.deleteFileContents("messages.xml");
		//add a message and than delete it.
		MessageData msgData = new MessageDataImp("test message to be deleted.");
		Message msg = new Message(msgData,1,_ps.getCurrentMsgID());
		_ps.addMsg(msg);
		_ps.deleteMessage(_ps.getCurrentMsgID() - 1);
		Message msgFromDb = _ps.getMessage(_ps.getCurrentMsgID());
		assertTrue(msgFromDb == null);
	}


	public void testEditMessage() {
		String newMessageContent = "new test message content";
		//_ps.deleteFileContents("messages.xml");
		MessageData msgData = new MessageDataImp("test message");
		Message msg = new Message(msgData,1,_ps.getCurrentMsgID());
		_ps.addMsg(msg);
		_ps.editMessage(_ps.getCurrentMsgID() - 1, new MessageDataImp(newMessageContent));
		Message msgFromDb = _ps.getMessage(_ps.getCurrentMsgID() - 1);
		assertTrue(msgFromDb.get_msgBody().toString().contentEquals(newMessageContent));
		
		//delete the message  we have added.
		//if we want to see the message on the data base comment next line.
		_ps.deleteMessage(_ps.getCurrentMsgID() - 1);
	}

	public void testGetMessage() {
		//_ps.deleteFileContents("messages.xml");
		MessageData msgData = new MessageDataImp("test message");
		Message msg = new Message(msgData,1,_ps.getCurrentMsgID());
		_ps.addMsg(msg);
		Message msgFromDb = _ps.getMessage(_ps.getCurrentMsgID() - 1);
		assertEquals(msg.get_mID(), msgFromDb.get_mID());
		
		//delete the message  we have added.
		//if we want to see the message on the data base comment next line.
		_ps.deleteMessage(_ps.getCurrentMsgID() - 1);
	}

	public void testGetUser() {
		//_ps.deleteFileContents("users.xml");
		
		RegisteredUser newUser = new RegisteredUser("userTest1",_ps.getCurrentUserID());
		_ps.addUser(newUser, "pass1");
		RegisteredUser userFromDb = _ps.getUser(_ps.getCurrentUserID()-1);
		assertEquals(newUser.get_uID(),userFromDb.get_uID());
		System.out.println(_ps.getCurrentUserID() - 1);
		_ps.deletePassword(_ps.getCurrentUserID() - 1);
		_ps.deleteUser("userTest1");
	}

	public void testGetUserPassword() {
		//_ps.deleteFileContents("users.xml");
		//_ps.deleteFileContents("passes.xml");
		//_ps.deleteUser("user1");
		RegisteredUser newUser = new RegisteredUser("userTest1",_ps.getCurrentUserID());
		_ps.addUser(newUser, "pass1");
		String newUserPassFromDB = _ps.getUserPassword(newUser.get_uID());
		assertTrue(newUserPassFromDB.contentEquals("pass1"));
		System.out.println(_ps.getCurrentUserID() - 1);
		_ps.deletePassword(_ps.getCurrentUserID() - 1);
		_ps.deleteUser("userTest1");//last time delete the user message.
	}

	public void testGetMessagesWithFather() {
		//_ps.deleteFileContents("messages.xml");
		MessageData msgData = new MessageDataImp("test message");
		//message poster is with id 1 , just for testing purposes.
		Message msg1 = new Message(msgData,1,_ps.getCurrentMsgID());
		Message msg2 = new Message(msgData,1,_ps.getCurrentMsgID(),_ps.getCurrentMsgID()+1);
		Message msg3 = new Message(msgData,1,_ps.getCurrentMsgID(),_ps.getCurrentMsgID()+2);
		_ps.addMsg(msg1);
		_ps.addMsg(msg2);
		_ps.addMsg(msg3);
		Collection<Message> allMsgs = _ps.getMessagesWithFather(_ps.getCurrentMsgID()-3);
		assertTrue(allMsgs.size() == 2);
		
		//delete all added messages from db.
		_ps.deleteMessage(_ps.getCurrentMsgID() - 3);
		_ps.deleteMessage(_ps.getCurrentMsgID() - 2);
		_ps.deleteMessage(_ps.getCurrentMsgID() - 1);
	}

}
