<<<<<<< .mine
package tests;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import implementation.Message;
import implementation.MessageDataImp;
import implementation.PersistenceSystemXML;
import implementation.RegisteredUser;
import domainLayer.MessageData;
import domainLayer.PersistenceSystem;
import junit.framework.TestCase;

public class testPresisLayer extends TestCase {
	PersistenceSystem ps ;
	RegisteredUser ru1;
	RegisteredUser ru2;
	Message m1;
	Message m2;
	


//	ps.addUser(ru1);
//	ps.addUser(ru2);
//	ps.addUser(ru3);
//	
//	RegisteredUser ud2 = ps.getUser(2);
//	RegisteredUser ud3 = ps.getUser(3);
//	RegisteredUser ud1 = ps.getUser(1);
//	
	public testPresisLayer(String name) {
		super(name);
		
		 
	}

	protected void setUp() throws Exception {
		super.setUp();
		 ps=new PersistenceSystemXML();
		 ru1 = new RegisteredUser("Admin",1);
		 ru2 = new RegisteredUser("Admin",2);
		 m1 = new Message(new MessageDataImp("Hello World1"),1,0,1);
		 m2 = new Message(new MessageDataImp("Hello World2"),2,0,2);
			 
	}
	public void testAddMessage(){
		ps.addMsg(m1);
		ps.addMsg(m2);
		Message test=ps.getMessage(1);
		Message test2=ps.getMessage(2);
		assertEquals(test,m1);
		assertEquals(test2,m2);
	}
	public void testAddUser(){
		ps.addUser(ru1,"pass1");
		ps.addUser(ru2,"pass2");
		assertEquals(ru1,ps.getUser(1));
		assertEquals(ru2,ps.getUser(2));
	}
	public void testRemoveUser(){
		ps.deleteUser(1);
		assertNull(ps.getUser(1));
	}
	public void testRemoveMessage(){
		ps.deleteMessage(1);
		assertNull(ps.getMessage(1));
	}
	public void  testEditMessage () {
//		ps.addMsg(m1);
//		MessageDataImp newMsg=new MessageDataImp("we change you all");
//		ps.editMessage(1, newMsg);
//		Message testEdit=ps.getMessage(1);
//		assertEquals("we change you all",testEdit.get_msgBody().toString());
		assertTrue(true);
	}
	




}
=======
package tests;

import implementation.Message;
import implementation.MessageDataImp;
import implementation.PersistenceSystemXML;
import implementation.RegisteredUser;
import domainLayer.MessageData;
import domainLayer.PersistenceSystem;
import junit.framework.TestCase;

public class testPresisLayer extends TestCase {
	PersistenceSystem ps ;
	RegisteredUser ru1;
	RegisteredUser ru2;
	Message m1;
	Message m2;
	


//	ps.addUser(ru1);
//	ps.addUser(ru2);
//	ps.addUser(ru3);
//	
//	RegisteredUser ud2 = ps.getUser(2);
//	RegisteredUser ud3 = ps.getUser(3);
//	RegisteredUser ud1 = ps.getUser(1);
//	
	public testPresisLayer(String name) {
		super(name);
		
		 
	}

	protected void setUp() throws Exception {
		super.setUp();
		 ps=new PersistenceSystemXML();
		 ru1 = new RegisteredUser("Admin","admin",1);
		 ru2 = new RegisteredUser("Admin","admin",2);
		 m1 = new Message(new MessageDataImp("Hello World1"),1,0,1);
		 m2 = new Message(new MessageDataImp("Hello World2"),2,0,2);
			 
	}
	public void testAddMessage(){
		ps.addMsg(m1);
		ps.addMsg(m2);
		Message test=ps.getMessage(1);
		Message test2=ps.getMessage(2);
		assertEquals(test.get_mID(),m1.get_mID());
		assertEquals(test.get_msgBody().toString(),m1.get_msgBody().toString());
		assertEquals(test2.get_mID(),m2.get_mID());
		assertEquals(test2.get_msgBody().toString(),m2.get_msgBody().toString());
	}
	public void testAddUser(){
		ps.addUser(ru1);
		ps.addUser(ru2);
		assertEquals(ru1.get_uID(),ps.getUser(1).get_uID());
		assertEquals(ru1.get_userName(), ps.getUser(1).get_userName());
		assertEquals(ru2.get_uID(),ps.getUser(2).get_uID());
		assertEquals(ru2.get_userName(), ps.getUser(2).get_userName());
	}
	
	public void testRemoveUser(){
		ps.deleteUser(1);
		assertNull(ps.getUser(1));
	}
	public void testRemoveMessage(){
		ps.deleteMessage(1);
		assertNull(ps.getMessage(1));
	}
	public void  testEditMessage () {
		ps.addMsg(m1);
		MessageDataImp newMsg=new MessageDataImp("we change you all");
		ps.editMessage(1, newMsg);
		Message testEdit=ps.getMessage(1);
		assertEquals("we change you all",testEdit.get_msgBody().toString());
		assertTrue(true);
	}
	




}
>>>>>>> .r8
