package tests;
//
//
//
//import Exceptions.UserAlreadyExistsException;
//
//import com.thoughtworks.xstream.XStream;
//import com.thoughtworks.xstream.io.xml.DomDriver;
//
//import domainLayer.PersistenceSystem;
//import domainLayer.TheController;
//import domainLayer.userData;
//
//import implementation.Message;
//import implementation.MessageDataImp;
//import implementation.PersistenceSystemXML;
//import implementation.RegisteredUser;
//
public class TestMain {
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		TheController tc = new TheController();
//		try {
//			tc.registerNewUser("user1", "pass1");
//		} catch (UserAlreadyExistsException e) {
//			e.printStackTrace();
//		}
//		
//		PersistenceSystem ps = new PersistenceSystemXML();
//
//		
//		RegisteredUser ru1 = new RegisteredUser("Admin",1);
//		RegisteredUser ru2 = new RegisteredUser("Admin",2);
//		RegisteredUser ru3 = new RegisteredUser("Admin",3);
//
//		ps.addUser(ru1,"pass1");
//		ps.addUser(ru2,"pass2");
//		ps.addUser(ru3,"pass3");
//		
//		RegisteredUser ud2 = ps.getUser(2);
//		RegisteredUser ud3 = ps.getUser(3);
//		RegisteredUser ud1 = ps.getUser(1);
//		
//		System.out.println("uid1 = " + ud1.get_uID());
//		System.out.println("uid2 = " + ud2.get_uID());
//		System.out.println("uid3 = " + ud3.get_uID());
//
//
//		ps.addUser(ru1);
//		ps.addUser(ru2);
//		ps.addUser(ru3);
//		
//		RegisteredUser ud2 = ps.getUser(2);
//		RegisteredUser ud3 = ps.getUser(3);
//		RegisteredUser ud1 = ps.getUser(1);
//		
//		System.out.println("uid1 = " + ud1.get_uID());
//		System.out.println("uid2 = " + ud2.get_uID());
//		System.out.println("uid3 = " + ud3.get_uID());
//
//
//		Message m1 = new Message(new MessageDataImp("Hello World1"),2,0,1);
//		Message m2 = new Message(new MessageDataImp("Hello World2"),2,0,2);
//		Message m3 = new Message(new MessageDataImp("Hello World3"),1,0,3);
//		
//		ps.addMsg(m1);
//		ps.addMsg(m2);
//		ps.addMsg(m3);
//		
//		Message m4 = ps.getMessage(2);
//		Message m5 = ps.getMessage(1);
//		Message m6 = ps.getMessage(3);
//		
//		System.out.println(m5.get_msgBody().toString());
//		System.out.println(m4.get_msgBody().toString());
//		System.out.println(m6.get_msgBody().toString());
//		
//		ps.deleteMessage(2);
//		
//	}
//
}
