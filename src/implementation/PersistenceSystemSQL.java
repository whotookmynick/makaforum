package implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import searchEngine.SearchTable;
import domainLayer.MessageData;
import domainLayer.PersistenceSystem;

public class PersistenceSystemSQL implements PersistenceSystem{
	
	private Connection createConnection() throws ClassNotFoundException, SQLException{
		String driver = "net.sourceforge.jtds.jdbc.Driver";
		Class.forName(driver);
		//String connString = "Provider=SQLOLEDB.1;Password=maka;Persist Security Info=True;User ID=maka;Initial Catalog=MAKA;Data Source=localhost\\SQLEXPRESS";
		Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.0.1:1433","maka","maka");
		return conn;
	}

	@Override
	public void addMsg(Message msg) {
		try{
		Connection conn = createConnection();
		String sql = "INSERT INTO [MAKAFORUM].[dbo].[Messages] (mID,msgPosterID,fatherMessageID,msgPostTime,msgElement,msgData_ID) " +
					" VALUES (" + msg.get_mID()+"," + msg.get_msgPosterID() +"," + msg.get_fatherMessageID()
					+"," + msg.get_msgPostTime() +",'" + msg.get_msgBody().toString() +"'," + msg.get_msgBody().getMid()+");";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.execute();
		}
		catch (Exception e) {}
	}

	@Override
	public void addUser(RegisteredUser ud, String password) {
		try{
			Connection conn = createConnection();
			String addUserSQL = "INSERT INTO [MAKAFORUM].[dbo].[Users] (LastLoginTime,SignupTime,NumberOfMessages,uID,UserName,UserType) " +
						" VALUES (" + ud.get_lastLogInTime()+"," + ud.get_signUpTime() +"," + ud.get_numOfMessages()
						+"," + ud.get_uID() +",'" + ud.get_userName() +"'," + ud.get_userType()+");";
			String addPassSQL = "INSERT INTO [MAKAFORUM].[dbo].[Passwords] (UserId, password) " +
			" VALUES (" + ud.get_uID() +",'" + password +"');";
			
			PreparedStatement addUserPS = conn.prepareStatement(addUserSQL);
			PreparedStatement addPassPS = conn.prepareStatement(addPassSQL);
			addUserPS.execute();
			addPassPS.execute();
			}
			catch (Exception e) {}
		
	}

	@Override
	public boolean changeUserPassword(long uid, String newPass) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ConcurrentHashMap<String, Long> createHashTableofUserNametoUID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message deleteMessage(long mid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void editMessage(long mid, MessageData newMsg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCurrentMsgID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentUserID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Message getMessage(long mid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Message> getMessagesWithFather(long fatherID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RegisteredUser getUser(long uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUserPassword(long uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void incMsgId() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void incUserId() {
		// TODO Auto-generated method stub
		
	}

}
