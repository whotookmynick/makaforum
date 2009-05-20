package implementation;

import java.io.IOException;

import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;



public class MakaLogger {
	private Logger logger ;
	private Date now=new Date();
	public MakaLogger(boolean append){

	this.logger = Logger.getLogger("my_log");
	Handler fh= null;
	try{
	fh = new FileHandler("Maka_log.txt",append);
	}catch(IOException e){System.out.println("error");}
	fh.setFormatter(new Formatter(){
		public String format(LogRecord record){
			final String result  = record.getMessage();


			return result;
		}

	});
	logger.addHandler(fh);
	logger.setLevel(Level.FINEST);

	}

	public void addNewMessage(long UserID,long MessageId,long fatherMessageId){
		this.logger.fine("New message add by user " +UserID+ " The message I.D is: " + MessageId + " to father message :"+fatherMessageId+ " ,( Log in : "+ this.now+" )"+ "\r\n");
	}
	public void EditeMessage(long UserID,long MessageId,long fatherMessageId){
		this.logger.fine("Message has  edit by " +UserID+ " The message I.D is: " + MessageId + " message father :"+fatherMessageId + " ,( Log in : "+ this.now+" )"+ "\r\n");
	}
	public void DeleteMessage(long UserID,long MessageId,long fatherMessageId){
		this.logger.fine("Message has  delete  The message I.D is: " + MessageId + " message father :"+fatherMessageId +"the user i.d: " +UserID+  " ,( Log in : "+ this.now+" )"+ "\r\n" );
	}
	public void registerUser(long UserID){
		this.logger.fine("User has  register The user I.D is: " + UserID + " ,( Log in : "+ this.now+" )"+ "\r\n");
	}
	public void ChangeRegisterType(long UserID,long UserID1){
		this.logger.fine("User changes Type,  The user I.D is: " + UserID + " give the new type" +UserID1 +" ,( Log in : "+ this.now+" )"+ "\r\n");
	}

}
