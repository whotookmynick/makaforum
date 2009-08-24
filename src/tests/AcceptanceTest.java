package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import implementation.ControlerFactory;
import implementation.Message;
import implementation.MessageDataImp;
import implementation.RegisteredUser;

import org.junit.Before;
import org.junit.Test;

import Exceptions.UserAlreadyExistsException;
import Exceptions.UserDoesNotExistException;

import searchEngine.Search;

import domainLayer.MessageData;
import domainLayer.TheController;
import junit.framework.TestCase;

public class AcceptanceTest extends TestCase{
	TheController _contorller;
	private Search _searchEngine;
	private long _msgPosterId;
	RegisteredUser currentUser;
	private long _msgPostTime;
	private final String _messageData = "test message for search1";
	
	@Before
	public void setUp() throws Exception {
		
		_contorller = ControlerFactory.getControler();
		_searchEngine = _contorller.get_searchEngine();
		
		currentUser = _contorller.getUser("user2New");
		if (currentUser == null)
		{
			_contorller.registerNewUser("user2New", "pass2");
		}
		currentUser = _contorller.logMeIn("user2New", "pass2");
		_msgPosterId = currentUser.get_uID();
		MessageDataImp mdi = new MessageDataImp(_messageData);
		_contorller.addNewMessage(mdi, currentUser);
		_msgPostTime = System.currentTimeMillis() - 60000;
	}
	
	
	@Test
	public void testRegisterNewUser() {
		try {
			if (_contorller.getUser("user1New") == null)
			{
				_contorller.registerNewUser("user1New", "password1");
			}
			assertTrue(_contorller.logMeIn("user1New", "password1") != null);
			assertTrue(_contorller.logMeIn("user1New", "fake pass") == null);
		} catch (UserDoesNotExistException e) {
			/*e.printStackTrace();*/
		} catch (UserAlreadyExistsException e) {
			/*e.printStackTrace();*/
		}
	}
	
	@Test
	public void testAddNewMessage() {
		String messageContent = "test new added message";
		MessageData msgData = new MessageDataImp(messageContent);
		_contorller.addNewMessage(msgData,currentUser);
		Iterator<Message> it = _contorller.getAllMessagesChildren(-1).iterator();
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
	public void testSearchByAuthor() {
		Collection<Message> retMsgs = _searchEngine.searchByAuthor("user2New");
		Iterator<Message> it = retMsgs.iterator();
		boolean found = false;
		while (it.hasNext() && !found){
			Message curr = it.next();
			found = curr.get_msgPosterID() == _msgPosterId;
		}
		assertTrue(found);
	}

	@Test
	public void testSearchByContent() {
		Collection<Message> retMsgs = _searchEngine.searchByContent(_messageData);
		Iterator<Message> it = retMsgs.iterator();
		boolean found = false;
		while (it.hasNext() && !found){
			Message curr = it.next();
			found = curr.get_msgBody().toString().contentEquals(_messageData);
		}
		assertTrue(found);
	}

	@Test
	public void testSearchByDate() {
		Date fromDate = new Date(_msgPostTime);
		Date toDate = new Date(_msgPostTime + 3600000);//the message post time plus one hour
		Collection<Message> retMsgs = _searchEngine.searchByDate(fromDate,toDate);
		Iterator<Message> it = retMsgs.iterator();
		boolean found = false;
		while (it.hasNext() && !found){
			Message curr = it.next();
			//  Date temp = new Date(curr.get_msgPostTime());
			  assertTrue((curr.get_msgPostTime() >= _msgPostTime) &&
					  (curr.get_msgPostTime() <= _msgPostTime + 3600000));
			found =  curr.get_msgBody().toString().contentEquals(_messageData);
		}
		assertTrue(found);
	}
	
	@Test
	public void testReplyToMessage() {
		String data = "this is the reply message." + System.currentTimeMillis();
		MessageData md = new MessageDataImp(data);
		try {
			if (_contorller.getUser("user1New") == null)
			{
				_contorller.registerNewUser("user1New", "password1");
			}
			RegisteredUser newRu = _contorller.logMeIn("user1New", "password1");
			_contorller.replyToMessage(md, newRu, _msgPosterId);
			Collection <Message> coll = _contorller.getAllMessagesChildren(_msgPosterId);
			Iterator<Message> it = coll.iterator();
			boolean found = false;
			while (it.hasNext() && !found){
				Message curr = it.next();
				found = curr.get_msgBody().toString().contentEquals(data);
			}
			assertTrue(found);
		}catch(UserAlreadyExistsException e){
			
		}catch(Exception e){}
	}
	
	/**
	 * checks if a user can delete his own message.
	 * checks if another user, can delete other user message.
	 */
	@Test
	public void testDeleteMessage() {
		String messageContent = "test message 1 to be deleted";
		String messageContent2 = "test message 2 should not be deleted";
		MessageData msgData = new MessageDataImp(messageContent);
		MessageData msgData2 = new MessageDataImp(messageContent2);
		RegisteredUser user3 = null,user4 = null;
		try {
			if(_contorller.getUser("user3New") == null){
				_contorller.registerNewUser("user3New", "pass3");
			}
			user3 = _contorller.logMeIn("user3New", "pass3");
			if(_contorller.getUser("user4New") == null){
				_contorller.registerNewUser("user4New", "pass4");
			}
			user4 = _contorller.logMeIn("user4New", "pass4");
			
		} catch (UserAlreadyExistsException e) {
			
		} catch (UserDoesNotExistException e) {
			
		}
		assertTrue((user3 != null) && (user4 != null));
		if(user3 != null && user4 != null){
			_contorller.addNewMessage(msgData,user3);
			_contorller.addNewMessage(msgData2,user3);
			Iterator<Message> it = _contorller.getAllMessagesChildren(-1).iterator();
			while (it.hasNext())
			{
				Message curr = it.next();
				if (curr.get_msgBody().toString().contentEquals(messageContent))
				{
					assertTrue(_contorller.deleteMessage(user3,curr.get_mID()));
				}else if(curr.get_msgBody().toString().contentEquals(messageContent2))
				{
					assertFalse(_contorller.deleteMessage(user4,curr.get_mID()));
				}
			}
		}
	}
	
	@Test
	public void testEditMsg() {
		String messageContent = "old test message";
		String newMessageContent = "edited test message for acceptance test";
		MessageData msgData = new MessageDataImp(messageContent);
		RegisteredUser ru=null;
		try {
			if(_contorller.getUser("user5New") == null)
				_contorller.registerNewUser("user5New", "pass5");
			ru = _contorller.logMeIn("user5New", "pass5");
		} catch (UserAlreadyExistsException e) {
			
		} catch (UserDoesNotExistException e) {
			
		}
		assertTrue(ru != null);
		if(ru != null){
			_contorller.addNewMessage(msgData, ru);
			
			Iterator<Message> it = _contorller.getAllMessagesChildren(-1).iterator();
			while (it.hasNext())
			{
				Message curr = it.next();
				if (curr.get_msgBody().toString().contentEquals(messageContent))
				{
					_contorller.editMsg(ru, curr.get_mID(), new MessageDataImp(newMessageContent));
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
}
