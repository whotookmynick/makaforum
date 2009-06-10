package searchEngine;

import implementation.Message;

import java.util.Date;
import java.util.Vector;

public interface Search {
	public Vector<Message> searchByAuthor(String authorName);
	public Vector<Message> searchByDate(Date from, Date to);
	public Vector<Message> searchByContent(String sentence);
	public void insertMessageToEngine(Message msg);
	public void removeMessageFromEngine(long mid);
}
