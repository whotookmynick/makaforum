package searchEngine;

import java.util.Collection;

import implementation.Message;

public interface DataTable {
	public boolean insertWord(String word);
	public boolean insertMessageFromWord(String word, Message message);
	public long getWordId(String word);
	public Collection <Long> getMessageId(long wordId);
}
