package tests;

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
		fail("Not yet implemented");
	}

	public void testAddUser() {
		RegisteredUser newUser = new RegisteredUser("user1",1);
		_ps.addUser(newUser, "pass1");
		RegisteredUser userFromDb = _ps.getUser(1);
		assertEquals(newUser.get_uID(),userFromDb.get_uID());
		
	}

	public void testChangeUserPassword() {
		fail("Not yet implemented");
	}

	public void testDeleteMessage() {
		fail("Not yet implemented");
	}

	public void testDeleteUser() {
		fail("Not yet implemented");
	}

	public void testEditMessage() {
		fail("Not yet implemented");
	}

	public void testGetMessage() {
		fail("Not yet implemented");
	}

	public void testGetUser() {
		fail("Not yet implemented");
	}

	public void testGetUserPassword() {
		fail("Not yet implemented");
	}

	public void testGetMessagesWithFather() {
		fail("Not yet implemented");
	}

}
