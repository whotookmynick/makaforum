package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Exceptions.UserDoesNotExistException;

import domainLayer.TheController;

public class TheControllerTest {
	TheController _controler;
	@Before
	public void setUp() throws Exception {
		_controler = new TheController();
	}

	@Test
	public void testLogMeIn() {
		try {
			assertTrue(_controler.logMeIn("user1", "pass1") != null);
			assertFalse(_controler.logMeIn("user1", "fake pass") != null);
		} catch (UserDoesNotExistException e) {
			e.printStackTrace();
		}
		
	}

	@Test
	public void testRegisterNewUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewMessage() {
		fail("Not yet implemented");
	}

	@Test
	public void testEditMsg() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAllMessagesChildren() {
		fail("Not yet implemented");
	}

	@Test
	public void testReplyToMessage() {
		fail("Not yet implemented");
	}

}
