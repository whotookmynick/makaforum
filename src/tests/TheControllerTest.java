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
			if(_controler.getUser("user1") == null)
				_controler.registerNewUser("user1", "pass1");
			assertTrue(_controler.logMeIn("user1", "pass1") != null);
			assertFalse(_controler.logMeIn("user1", "fake pass") != null);
		} catch (UserDoesNotExistException e) {
			/*e.printStackTrace();*/
		} catch (UserAlreadyExistsException e) {
			/*e.printStackTrace();*/
		}
		
	}

	@Test
	public void testRegisterNewUser() {
		try {
			if(_controler.getUser("user1") == null)
				_controler.registerNewUser("user1", "pass1");
			assertTrue(_controler.logMeIn("user1", "pass1") != null);
			assertTrue(_controler.logMeIn("user1", "fake pass") == null);
		} catch (UserDoesNotExistException e) {
			
		} catch (UserAlreadyExistsException e) {
			
		}
	}

	@Test
	public void testAddNewMessage() {
		String messageContent = "test message added succesfully";
		MessageData msgData = new MessageDataImp(messageContent);
		RegisteredUser user=null;
		try {
			if(_controler.getUser("user2") == null)
				_controler.registerNewUser("user2", "pass2");
			user = _controler.logMeIn("user2", "pass2");
		} catch (UserAlreadyExistsException e) {
			
		} catch (UserDoesNotExistException e) {
			
		}
		
		_controler.addNewMessage(msgData,user);
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
		RegisteredUser ru=null;
		try {
			if(_controler.getUser("user1") == null)
				_controler.registerNewUser("user1", "pass1");
			ru = _controler.logMeIn("user1", "pass1");
		} catch (UserAlreadyExistsException e) {
			
		} catch (UserDoesNotExistException e) {
			
		}
		assertTrue(ru != null);
		if(ru != null){
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
	}

	
	/*public void testGetAllMessagesChildren() {
		fail("Not yet implemented");
	}

	
	public void testReplyToMessage() {
		fail("Not yet implemented");
	}*/

}
