package tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import implementation.ControlerFactory;
import implementation.Message;
import implementation.MessageDataImp;
import implementation.RegisteredUser;

import org.junit.Before;
import org.junit.Test;

import Exceptions.UserAlreadyExistsException;
import Exceptions.UserDoesNotExistException;

import domainLayer.MessageData;
import domainLayer.TheController;

public class TheControllerTest {
	TheController _controler;
	@Before
	public void setUp() throws Exception {
		_controler = ControlerFactory.getControler();	
	}

	@Test
	public void testLogMeIn() {
		try {
			_controler.registerNewUser("user1", "pass1");
			assertTrue(_controler.logMeIn("user1", "pass1") != null);
			assertFalse(_controler.logMeIn("user1", "fake pass") != null);
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testRegisterNewUser() {
		try {
			_controler.registerNewUser("user1", "pass1");
			assertTrue(_controler.logMeIn("user1", "pass1") != null);
			assertFalse(_controler.logMeIn("user1", "fake pass") == null);
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAddNewMessage() {
		String messageContent = "test message";
		MessageData msgData = new MessageDataImp(messageContent);
		try {
			_controler.registerNewUser("user2", "pass2");
			_controler.logMeIn("user2", "pass2");
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		}
		_controler.addNewMessage(msgData,_controler.getUser("user2") );
		Iterator<Message> it = _controler.getAllMessagesChildren(-1).iterator();
		while (it.hasNext())
		{
			Message curr = it.next();
			if (curr.get_msgBody().toString().contentEquals(messageContent))
			{
				assertTrue(curr.get_msgBody().toString().contentEquals(messageContent));
			}
		}
	}

	@Test
	public void testEditMsg() {
		String messageContent = "test message";
		String newMessageContent = "edited test message";
		MessageData msgData = new MessageDataImp(messageContent);
		try {
			_controler.registerNewUser("user1", "pass1");
			_controler.logMeIn("user1", "pass1");
		} catch (UserAlreadyExistsException e) {
			e.printStackTrace();
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		}
		RegisteredUser ru = _controler.getUser("user1");
		_controler.addNewMessage(msgData, ru);
		
		Iterator<Message> it = _controler.getAllMessagesChildren(-1).iterator();
		while (it.hasNext())
		{
			Message curr = it.next();
			if (curr.get_msgBody().toString().contentEquals(messageContent))
			{
				_controler.editMsg(ru, curr.get_mID(), new MessageDataImp(newMessageContent));
			}
		}
		while (it.hasNext())
		{
			Message curr = it.next();
			if (curr.get_msgBody().toString().contentEquals(messageContent))
			{
				assertTrue(curr.get_msgBody().toString().contentEquals(newMessageContent));
			}
		}
	}

	
	/*public void testGetAllMessagesChildren() {
		fail("Not yet implemented");
	}

	
	public void testReplyToMessage() {
		fail("Not yet implemented");
	}*/

}
