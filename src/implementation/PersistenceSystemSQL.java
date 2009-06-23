package implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import domainLayer.MessageData;
import domainLayer.PersistenceSystem;

// TODO: Auto-generated Javadoc
/**
 * The Class PersistenceSystemSQL.
 */
public class PersistenceSystemSQL implements PersistenceSystem{
	
	/**
	 * Creates the connection.
	 * 
	 * @return the connection
	 * 
	 * @throws ClassNotFoundException the class not found exception
	 * @throws SQLException the SQL exception
	 */
	private Connection createConnection() throws ClassNotFoundException, SQLException{
		String driver = "net.sourceforge.jtds.jdbc.Driver";
		Class.forName(driver);
		//String connString = "Provider=SQLOLEDB.1;Password=maka;Persist Security Info=True;User ID=maka;Initial Catalog=MAKA;Data Source=localhost\\SQLEXPRESS";
		Connection conn = DriverManager.getConnection("jdbc:jtds:sqlserver://10.0.0.1:1433","maka","maka");
		return conn;
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#addMsg(implementation.Message)
	 */
	@Override
	public void addMsg(Message msg) {
		try{
		Connection conn = createConnection();
		String MsgSQL = "INSERT INTO [MAKAFORUM].[dbo].[Messages] (mID,msgPosterID,fatherMessageID,msgPostTime,msgElement,msgData_ID) " +
					" VALUES (" + msg.get_mID()+"," + msg.get_msgPosterID() +"," + msg.get_fatherMessageID()
					+"," + msg.get_msgPostTime() +",'" + msg.get_msgBody().toString() +"'," + msg.get_msgBody().getMid()+");";
		PreparedStatement ps = conn.prepareStatement(MsgSQL);
		ps.execute();
		}
		catch (Exception e) {e.printStackTrace();}
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#addUser(implementation.RegisteredUser, java.lang.String)
	 */
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
			catch (Exception e) {e.printStackTrace();}
		
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#changeUserPassword(long, java.lang.String)
	 */
	@Override
	public boolean changeUserPassword(long uid, String newPass) {
		try{
			Connection conn = createConnection();
			String changePassSQL = "UPDATE [MAKAFORUM].[dbo].[Passwords] SET password = '" + newPass +
			"' WHERE UserId =" + uid + ";";
			
			PreparedStatement changePassPS = conn.prepareStatement(changePassSQL);
			changePassPS.execute();
			}
			catch (Exception e) {e.printStackTrace();}
		return false;
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#createHashTableofUserNametoUID()
	 */
	@Override
	public ConcurrentHashMap<String, Long> createHashTableofUserNametoUID() {
		ConcurrentHashMap<String, Long> UserNametoUidTable = new ConcurrentHashMap<String, Long>();
		try{
			Connection conn = createConnection();
			String UserNametoUidSQL = "SELECT uID,UserName FROM [MAKAFORUM].[dbo].[Users];";
			PreparedStatement ps = conn.prepareStatement(UserNametoUidSQL);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				long uId = rs.getInt("uID");
				String userName = rs.getString("UserName");
				UserNametoUidTable.put(userName, uId);
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return UserNametoUidTable;
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#deleteMessage(long)
	 */
	@Override
	public Message deleteMessage(long mid) {
		Message msg = getMessage(mid);
		try{
			Connection conn = createConnection();
			String deleteMsgSQL = "Delete FROM [MAKAFORUM].[dbo].[Messages] WHERE mID = " + mid+ ";";
			PreparedStatement deleteMsgSQLPS = conn.prepareStatement(deleteMsgSQL);
			deleteMsgSQLPS.execute();
			
		}
		catch (Exception e) {e.printStackTrace();}
		return msg;
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#editMessage(long, domainLayer.MessageData)
	 */
	@Override
	public void editMessage(long mid, MessageData newMsg) {
		try{
			Connection conn = createConnection();
			String editMsgSQL = "UPDATE [MAKAFORUM].[dbo].[Messages] SET msgElement = '" + newMsg.toString() +
			"' ,msgData_ID = " + newMsg.getMid() + "WHERE mID =" + mid + ";";
			
			PreparedStatement editMsgPS = conn.prepareStatement(editMsgSQL);
			editMsgPS.execute();
			}
			catch (Exception e) {e.printStackTrace();}
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#getCurrentMsgID()
	 */
	@Override
	public int getCurrentMsgID() {
		int mid = 0;
        try{
            Connection conn = createConnection();
            String currMsgSQL = "SELECT * FROM [MAKAFORUM].[dbo].[Messages];";
           
            PreparedStatement editMsgPS = conn.prepareStatement(currMsgSQL);
            ResultSet rs = editMsgPS.executeQuery();
            
            if (rs!=null){
                int j = 1;
                int i = rs.findColumn("mID");
                while(rs.next()){
                    if(j == 1){
                        mid = rs.getInt(i);
                    }
                    if(rs.getInt(i) > mid) 
                    	mid = rs.getInt(i);
                }
            }
        }
        catch (Exception e) {e.printStackTrace();}
        return mid+1;
    }


	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#getCurrentUserID()
	 */
	@Override
	public int getCurrentUserID() {
        int uid = 0;
        try{
            Connection conn = createConnection();
            String currMsgSQL = "SELECT * FROM [MAKAFORUM].[dbo].[Users];";
            PreparedStatement editMsgPS = conn.prepareStatement(currMsgSQL);
            ResultSet rs = editMsgPS.executeQuery();
            if (rs!=null){
                int j = 1;
                int i = rs.findColumn("uID");
                while(rs.next()){
                    if(j == 1){
                        uid = rs.getInt(i);
                    }
                    if(rs.getInt(i) > uid) 
                    	uid = rs.getInt(i);
                }
            }
        }
        catch (Exception e) {e.printStackTrace();}
        return uid+1;
    }

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#getMessage(long)
	 */
	@Override
	public Message getMessage(long mid) {
		Message msg = null;
		try{
			Connection conn = createConnection();
			String getMsgSQL = "SELECT * FROM [MAKAFORUM].[dbo].[Messages] WHERE mID = " + mid+ ";";
			PreparedStatement ps = conn.prepareStatement(getMsgSQL);
			ResultSet rs = ps.executeQuery();
			rs.next();
			long msgPosterID = rs.getLong("msgPosterID");
			long fatherMessageID = rs.getLong("fatherMessageID");
			long msgPostTime = rs.getLong("msgPostTime");
			String msgElement = rs.getString("msgElement");
			long msgData_ID = rs.getLong("msgData_ID");
			
			MessageData msgData= new MessageDataImp(msgElement);
			msgData.setMid(msgData_ID);
			msg= new Message(msgData, msgPosterID, fatherMessageID, mid, msgPostTime);
		}
		catch (Exception e) {e.printStackTrace();}
		return msg;
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#getMessagesWithFather(long)
	 */
	@Override
	public Collection<Message> getMessagesWithFather(long fatherID) {
		Collection<Message> msgVector = new Vector<Message>();
		try{
			Connection conn = createConnection();
			String UserNametoUidSQL = "SELECT * FROM [MAKAFORUM].[dbo].[Messages];";
			PreparedStatement ps = conn.prepareStatement(UserNametoUidSQL);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				long currentFatherID = rs.getLong("fatherMessageID");
				if (currentFatherID == fatherID){
					long msgPosterID = rs.getLong("msgPosterID");
					long mId = rs.getLong("mID");
					long msgPostTime = rs.getLong("msgPostTime");
					String msgElement = rs.getString("msgElement");
					long msgData_ID = rs.getLong("msgData_ID");
					MessageData msgData= new MessageDataImp(msgElement);
					msgData.setMid(msgData_ID);
					Message msg= new Message(msgData, msgPosterID, currentFatherID, mId, msgPostTime);
					msgVector.add(msg);
				}
			}
		}
		catch (Exception e) {e.printStackTrace();}
		return msgVector;
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#getUser(long)
	 */
	@Override
	public RegisteredUser getUser(long uid) {
		RegisteredUser registeredUser = null;
		try{
			Connection conn = createConnection();
			String getUserSQL = "SELECT * FROM [MAKAFORUM].[dbo].[Users] WHERE uID = " + uid+ ";";
			PreparedStatement ps = conn.prepareStatement(getUserSQL);
			ResultSet rs = ps.executeQuery();
			rs.next();
			long LastLoginTime = rs.getLong("LastLoginTime");
			long SignupTime = rs.getLong("SignupTime");
			int NumberOfMessages = rs.getInt("NumberOfMessages");
			String UserName = rs.getString("UserName");
			int UserType = rs.getInt("UserType");

			registeredUser = new RegisteredUser(LastLoginTime, SignupTime, NumberOfMessages, uid, UserName, UserType); 
		}
		catch (Exception e) {e.printStackTrace();}
		return registeredUser;
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#getUserPassword(long)
	 */
	@Override
	public String getUserPassword(long uid) {
        String pass = "";
        try{
            Connection conn = createConnection();
            String userPassSQL = "SELECT password FROM [MAKAFORUM].[dbo].[Passwords] WHERE UserId = " +
                                                                                            uid +";";
            PreparedStatement userPassPS = conn.prepareStatement(userPassSQL);
            ResultSet rs = userPassPS.executeQuery();
            if (rs!=null){
                while(rs.next()){
                    int i = rs.findColumn("password");
                    pass = rs.getString(i);

                }
            }
        }
        catch (Exception e) {e.printStackTrace();}
        return pass;
    }

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#incMsgId()
	 */
	@Override
	public void incMsgId() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see domainLayer.PersistenceSystem#incUserId()
	 */
	@Override
	public void incUserId() {
		// TODO Auto-generated method stub
		
	}

}
