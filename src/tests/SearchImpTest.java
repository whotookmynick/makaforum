package tests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import implementation.Message;
import implementation.MessageDataImp;

import implementation.RegisteredUser;

import org.junit.Before;
import org.junit.Test;



import domainLayer.TheController;

import searchEngine.Search;

public class SearchImpTest {

	private Search _searchEngine;
	private long _msgPosterId;
	private long _msgPostTime;
	private final String _messageData = "test message for search1";
		
	@Before
	public void setUp() throws Exception {
		TheController _contorller;
		_contorller = new TheController();
		_searchEngine = _contorller.get_searchEngine();
		RegisteredUser currentUser;
		currentUser = _contorller.getUser("searchTestUser1");
		if (currentUser == null)
		{
			_contorller.registerNewUser("searchTestUser1", "1234");
		}
		currentUser = _contorller.logMeIn("searchTestUser1", "1234");
		_msgPosterId = currentUser.get_uID();
		MessageDataImp mdi = new MessageDataImp(_messageData);
		_contorller.addNewMessage(mdi, currentUser);
		_msgPostTime = System.currentTimeMillis() - 60000;
	}

	@Test
	public void testSearchByAuthor() {
		Collection<Message> retMsgs = _searchEngine.searchByAuthor("searchTestUser1");
		Iterator<Message> it = retMsgs.iterator();
		boolean found = false;
		while (it.hasNext() && !found){
			Message curr = it.next();
			found = curr.get_msgPosterID() == _msgPosterId;
		}
		assertTrue(found);
	}

	
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
			found = curr.get_msgBody().toString().contentEquals(_messageData);
		}
		assertTrue(found);
	}

}
