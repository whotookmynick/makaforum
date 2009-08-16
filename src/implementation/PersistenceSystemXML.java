package implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import searchEngine.SearchTable;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import domainLayer.MessageData;
import domainLayer.PersistenceSystem;

public class PersistenceSystemXML implements PersistenceSystem,SearchTable {

	private static final String userFilePath = "users.xml";
	private static final String userTag = "OurUserTag";
	private static final String msgFilePath = "messages.xml";
	private static final String msgTag = "OurMessageTag";
	private static final String passFilePath = "passes.xml";
	private static final String passTag = "OurPassTag";
	private static final String idFilePath = "ids.xml";

	/**
	 * the files for the stable search engine.
	 */
	private static final String wordToWIDFilePath= "searchWords.xml";
	private static final String wIDToMsgIDFilePath= "searchMsgID.xml";


	@Override
	public void addMsg(Message msg) {
		writeObjectToFile(msg, msgFilePath, msgTag);
	}

	@Override
	public void addUser(RegisteredUser ru,String password) {
		writeObjectToFile(ru, userFilePath, userTag);
		long userId = ru.get_uID();
		writeObjectToFile(new UserPassword(userId,password),passFilePath,passTag);
	}

	@Override
	public boolean changeUserPassword(long uid, String newPass) {
		boolean endOfObject = false;
		boolean ans = false;
		try{

			Collection<UserPassword> allMessages = new Vector<UserPassword>();
        	File inFile = new File(passFilePath);
        	if (inFile.exists()){
        		BufferedReader inputStream =
        			new BufferedReader(new FileReader(inFile));
        		String l = "";
        		String serializedObject ="";
        		while ((l = inputStream.readLine()) != null) {
        			endOfObject = l.contains("</" + passTag + ">");
        			serializedObject +=l;
        			if (endOfObject) {
        				serializedObject = serializedObject.substring(2+passTag.length());
        				serializedObject = serializedObject.substring(0,serializedObject.indexOf("</" + passTag + ">"));
        				UserPassword desirializedObject = (UserPassword)deserializeObject(serializedObject);
        				if (desirializedObject.get_userID() == uid){
        					desirializedObject.set_password(newPass);
        					ans = true;
        				}
        				allMessages.add(desirializedObject);
        				serializedObject = "";
        			}
        		}
    			inputStream.close();
    			writeCollectionToFile(allMessages,passFilePath,passTag);
        	}
        }
		catch(Exception e){
			e.printStackTrace();
		}
    	return ans;
	}

	@Override
	/**
	 * Code copy pasted from delete getMessage
	 */
	public Message deleteMessage(long mid) {
		boolean endOfObject = false;
		Message returnMessage = null;
		try{

			Collection<Message> allMessages = new Vector<Message>();
        	File inFile = new File(msgFilePath);
        	if (inFile.exists()){
        		BufferedReader inputStream =
        			new BufferedReader(new FileReader(inFile));
        		String l = "";
        		String serializedObject ="";
        		while ((l = inputStream.readLine()) != null) {
        			endOfObject = l.contains("</" + msgTag + ">");
        			serializedObject +=l;
        			if (endOfObject) {
        				serializedObject = serializedObject.substring(2+msgTag.length());
        				serializedObject = serializedObject.substring(0,serializedObject.indexOf("</" + msgTag + ">"));
        				Message desirializedObject = (Message)deserializeObject(serializedObject);
        				if (desirializedObject.get_mID() != mid){
        					allMessages.add(desirializedObject);
        				}
        				else
        				{
        					returnMessage = desirializedObject;
        				}
        				serializedObject = "";
        			}
        		}
    			inputStream.close();
    			writeCollectionToFile(allMessages,msgFilePath,msgTag);
        	}
        }
		catch(Exception e){
			e.printStackTrace();
		}
    	return returnMessage;
	}

	private void writeCollectionToFile(Collection<?> c,String filePath,String tagToAdd) throws IOException{
		deleteFileContents(filePath);
		Iterator<?> it = c.iterator();
		while (it.hasNext()){
			Object current = it.next();
			writeObjectToFile(current, filePath, tagToAdd);
		}
	}

	@Override
	public void editMessage(long mid, MessageData newMsg) {
		Message editedMessage = deleteMessage(mid);
		editedMessage.set_msgBody(newMsg);
		addMsg(editedMessage);
	}

	@Override
	public Message getMessage(long mid) {
		boolean endOfObject = false;
		try{
        	File inFile = new File(msgFilePath);
        	if (inFile.exists()){
        		BufferedReader inputStream =
        			new BufferedReader(new FileReader(inFile));
        		String l = "";
        		String serializedObject ="";
        		while ((l = inputStream.readLine()) != null) {
        			endOfObject = l.contains("</" + msgTag + ">");
        			serializedObject +=l;
        			if (endOfObject) {
        				serializedObject = serializedObject.substring(2+msgTag.length());
        				serializedObject = serializedObject.substring(0,serializedObject.indexOf("</" + msgTag + ">"));
        				Message desirializedObject = (Message)deserializeObject(serializedObject);
        				if (desirializedObject.get_mID() == mid)
        					return desirializedObject;
        				serializedObject = "";
        			}
        		}
    			inputStream.close();
        	}
        }
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * VERY IMPORTANT: Note that this method is a copy paste of the getMsg method.
	 * If you change this message probably the other one needs to be changed.
	 */
	public RegisteredUser getUser(long uid){
			boolean endOfObject = false;
			try{
	        	File inFile = new File(userFilePath);
	        	if (inFile.exists()){
	        		BufferedReader inputStream =
	        			new BufferedReader(new FileReader(inFile));
	        		String l = "";
	        		String serializedObject ="";
	        		while ((l = inputStream.readLine()) != null) {
	        			endOfObject = l.contains("</" + userTag + ">");
	        			serializedObject +=l;
	        			if (endOfObject) {
	        				serializedObject = serializedObject.substring(2+userTag.length());
	        				serializedObject = serializedObject.substring(0,serializedObject.indexOf("</" + userTag + ">"));
	        				RegisteredUser desirializedObject = (RegisteredUser)deserializeObject(serializedObject);
	        				if (desirializedObject.get_uID() == uid)
	        					return desirializedObject;
	        				serializedObject = "";
	        			}
	        		}
	    			inputStream.close();
	        	}
	        }
			catch(Exception e){
				e.printStackTrace();
			}
			return null;
	}

	public String getUserPassword(long uid){
		try
		{
			boolean foundUser = false;
        	File inFile = new File(passFilePath);
        	if (inFile.exists()){
        		BufferedReader inputStream =
        			new BufferedReader(new FileReader(inFile));
        		while (!foundUser && inputStream.ready()){
        			UserPassword up;
        			up = (UserPassword)getObjectFromFile(inputStream, passTag);
        			if (up.get_userID() == uid)
        			{
        				return up.get_password();
        			}
        		}
        	}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public Collection<Message> getMessagesWithFather(long fatherID){
		try
		{
			Collection<Message> ans = new Vector<Message>();
        	File inFile = new File(msgFilePath);
        	if (inFile.exists()){
        		BufferedReader inputStream =
        			new BufferedReader(new FileReader(inFile));
        		while (inputStream.ready()){
        			Message ms;
        			ms = (Message)getObjectFromFile(inputStream, msgTag);
        			if (ms.get_fatherMessageID() == fatherID)
        				ans.add(ms);
        		}
        		return ans;
        	}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return new Vector<Message>();
	}

	public Object fromXMLFile(String filePath) {

		System.out.println("Loading from: " + filePath);
        try{
        	File inFile = new File(filePath);
        	if (inFile.exists()){
        		BufferedReader inputStream =
        			new BufferedReader(new FileReader(inFile));
        		String l = "";
        		String serializedObject ="";
        		while ((l = inputStream.readLine()) != null) {
        			serializedObject +=l;
        		}
        		Object desirializedObject = deserializeObject(serializedObject);
        		inputStream.close();
        		return desirializedObject;
        	}
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return null;
    }

	/**
	 * A simple serializer that serializes all the league data and then writes
	 * it to an ouput file. This function uses JDOM and XSTREAM external packages.
	 */
	public void toXMLFile(Object whatToWriteToFile,String outfilePath) {
        synchronized (outfilePath) {
			System.out.println("Writing to XML, file " + outfilePath);
			try {
				PrintWriter outputStream = new PrintWriter(new FileWriter(
						outfilePath));
				String serializedLeague = serializeObject(whatToWriteToFile);
				outputStream.println(serializedLeague);
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }

	public static String serializeObject(Object o)
	{
		XStream xstream = new XStream(new DomDriver());
		return xstream.toXML(o);
	}

	public static Object deserializeObject(String s)
	{
		XStream xstream = new XStream(new DomDriver());
		return xstream.fromXML(s);
	}

	private static void writeObjectToFile(Object o,String filePath,String tagToAdd){
		synchronized (filePath) {
			String serializedObject = serializeObject(o);
			serializedObject = "<" + tagToAdd + ">" + serializedObject + "</"
					+ tagToAdd + ">";
			try {
				PrintWriter outputStream = new PrintWriter(new FileWriter(
						filePath, true));
				outputStream.println(serializedObject);
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static Object getObjectFromFile(BufferedReader inputStream,String tagToFind){
		boolean endOfObject = false;
		try{
        		String l = "";
        		String serializedObject ="";
        		while ((l = inputStream.readLine()) != null) {
        			endOfObject = l.contains("</" + tagToFind + ">");
        			serializedObject +=l;
        			if (endOfObject) {
        				serializedObject = serializedObject.substring(2+tagToFind.length());
        				serializedObject = serializedObject.substring(0,serializedObject.indexOf("</" + tagToFind + ">"));
        				Object desirializedObject = deserializeObject(serializedObject);
        				return desirializedObject;
        			}
        		}
    			inputStream.close();
        }
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ConcurrentHashMap<String, Long> createHashTableofUserNametoUID() {
		boolean endOfObject = false;
		ConcurrentHashMap<String,Long> ans = new ConcurrentHashMap<String, Long>();
		try{
        	File inFile = new File(userFilePath);
        	if (inFile.exists()){
        		BufferedReader inputStream =
        			new BufferedReader(new FileReader(inFile));
        		String l = "";
        		String serializedObject ="";
        		while ((l = inputStream.readLine()) != null) {
        			endOfObject = l.contains("</" + userTag + ">");
        			serializedObject +=l;
        			if (endOfObject) {
        				serializedObject = serializedObject.substring(2+userTag.length());
        				serializedObject = serializedObject.substring(0,serializedObject.indexOf("</" + userTag + ">"));
        				RegisteredUser desirializedObject = (RegisteredUser)deserializeObject(serializedObject);
        				ans.put(desirializedObject.get_userName(),desirializedObject.get_uID());
        				serializedObject = "";
        			}
        		}
    			inputStream.close();
        	}
        }
		catch(Exception e){
			e.printStackTrace();
		}
		return ans;
	}
	
	public void deleteFileContents(String filePath){
		//This next line deletes the data in the file
		try{
			PrintWriter outputStream = new PrintWriter(new FileWriter(filePath, false));
			outputStream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Collection<Long> Search_getMessageId(long wordId) {
		HashMap<Long, Collection<Long>> wordIdToMsgIdTable = (HashMap<Long, Collection<Long>>)fromXMLFile(wIDToMsgIDFilePath);
		return wordIdToMsgIdTable.get(new Long(wordId));
	}

	public long Search_getWordId(String word) {
		HashMap<String, Long> wordTable = (HashMap<String, Long>)fromXMLFile(wordToWIDFilePath);
		if(wordTable == null) return -1;
		if(!wordTable.containsKey(word)) return -2;
		return wordTable.get(word);
	}

	public boolean Search_insertMessageFromWord(String word, Message message, Collection<Long> emptyCollection) {
		//Search_insertWord(word); //make sure a tuple exists.
		boolean bInserted = false;
		long wordId = Search_getWordId(word);
		long msgId = message.get_mID();
		//get the table from the file :
		HashMap<Long, Collection<Long>> wordIdToMsgIdTable = (HashMap<Long, Collection<Long>>)fromXMLFile(wIDToMsgIDFilePath);
		if(wordIdToMsgIdTable == null)
			wordIdToMsgIdTable = new HashMap<Long, Collection<Long>>();
		//update the table :
		if(wordIdToMsgIdTable.containsKey(wordId)){
			Collection<Long> wordMsgs = wordIdToMsgIdTable.get(new Long(wordId));
			if (!wordMsgs.contains(new Long(msgId))){
				wordMsgs.add(new Long(msgId));
			}
			wordIdToMsgIdTable.put(wordId, wordMsgs);
			toXMLFile(wordIdToMsgIdTable, wIDToMsgIDFilePath);
			bInserted = true;
		}
		else{
			emptyCollection.add(new Long(msgId));
			wordIdToMsgIdTable.put(wordId, emptyCollection);
			toXMLFile(wordIdToMsgIdTable, wIDToMsgIDFilePath);
			bInserted = true;
		}
		return bInserted;
	}

	public boolean Search_insertWord(String word) {
		boolean wrote = false;
		HashMap<String, Long> wordTable = (HashMap<String, Long>)fromXMLFile(wordToWIDFilePath);
		if(wordTable == null) wordTable = new HashMap<String, Long>();

		if(!wordTable.containsKey(word)){

			Long newWordId = new Long(wordTable.size());
			wordTable.put(word, newWordId);
			toXMLFile(wordTable, wordToWIDFilePath);
			wrote = true;
		}
		return wrote;
	}

	public Vector<Message> Search_getAllMessages() {
		Vector<Message> allMessages = new Vector<Message>();
		boolean endOfObject = false;
		try{
        	File inFile = new File(msgFilePath);
        	if (inFile.exists()){
        		BufferedReader inputStream =
        			new BufferedReader(new FileReader(inFile));
        		String l = "";
        		String serializedObject ="";
        		while ((l = inputStream.readLine()) != null) {
        			endOfObject = l.contains("</" + msgTag + ">");
        			serializedObject +=l;
        			if (endOfObject) {
        				serializedObject = serializedObject.substring(2+msgTag.length());
        				serializedObject = serializedObject.substring(0,serializedObject.indexOf("</" + msgTag + ">"));
        				Message desirializedObject = (Message)deserializeObject(serializedObject);
        				allMessages.add(desirializedObject);
        				serializedObject = "";
        			}
        		}
    			inputStream.close();
        	}
        }
		catch(Exception e){
			e.printStackTrace();
		}
		return allMessages;
	}

	public long Search_getUserId(String userName){
		boolean endOfObject = false;
		try{
        	File inFile = new File(userFilePath);
        	if (inFile.exists()){
        		BufferedReader inputStream =
        			new BufferedReader(new FileReader(inFile));
        		String l = "";
        		String serializedObject ="";
        		while ((l = inputStream.readLine()) != null) {
        			endOfObject = l.contains("</" + userTag + ">");
        			serializedObject +=l;
        			if (endOfObject) {
        				serializedObject = serializedObject.substring(2+userTag.length());
        				serializedObject = serializedObject.substring(0,serializedObject.indexOf("</" + userTag + ">"));
        				RegisteredUser desirializedObject = (RegisteredUser)deserializeObject(serializedObject);
        				if (desirializedObject.get_userName().equals(userName))
        					return desirializedObject.get_uID();
        				serializedObject = "";
        			}
        		}
    			inputStream.close();
        	}
        }
		catch(Exception e){
			e.printStackTrace();
		}
		return -1;
}

	@Override
	public int getCurrentMsgID() {
		Vector<Integer> ids = (Vector<Integer>)fromXMLFile(idFilePath);
		return ids.get(1);
	}

	@Override
	public int getCurrentUserID() {
		Vector<Integer> ids = (Vector<Integer>)fromXMLFile(idFilePath);
		return ids.get(0);
	}

	@Override
	public void incMsgId(){
		Vector<Integer> ids = (Vector<Integer>)fromXMLFile(idFilePath);
		ids.set(1,ids.get(1) + 1);
		toXMLFile(ids, idFilePath);
	}

	@Override
	public void incUserId(){
		Vector<Integer> ids = (Vector<Integer>)fromXMLFile(idFilePath);
		ids.set(0,ids.get(0) + 1);
		toXMLFile(ids, idFilePath);
	}

}
