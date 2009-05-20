package searchEngine;

import java.util.Collection;
import java.util.Vector;

import implementation.Message;

public interface SearchTable{
	public boolean Search_insertWord(String word);
	public boolean Search_insertMessageFromWord(String word, Message message, Collection<Long> emptyCollection);
	public long Search_getWordId(String word);
	public Collection <Long> Search_getMessageId(long wordId);
	public Vector<Message> Search_getAllMessages();
	public long Search_getUserId(String userName);
	public Message getMessage(long mID);
}
