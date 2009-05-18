package searchEngine;

import java.util.Collection;

import implementation.Message;

public interface SearchTable{
	public boolean Search_insertWord(String word);
	public boolean Search_insertMessageFromWord(String word, Message message);
	public long Search_getWordId(String word);
	public Collection <Long> Search_getMessageId(long wordId);
}
