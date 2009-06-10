package searchEngine;

import implementation.Message;

import java.util.Date;
import java.util.Vector;

public class SearchImp implements Search{ //will extract the messages from the data table
	private SearchAgent _searchAgent;

	public SearchImp(SearchTable searchTable){
		_searchAgent = new SearchAgent(searchTable);
	}

	public void insertMessageToEngine(Message msg){
		_searchAgent.insertMessageToEngine(msg);
	}

	public SearchAgent getSearchAgent(){
		return _searchAgent;
	}

	public Vector <Message> searchByAuthor(String authorName) {
		long userId = _searchAgent.getSearchTable().Search_getUserId(authorName);
		Vector<Message> allMessages = _searchAgent.getSearchTable().Search_getAllMessages();
		Vector<Message> authorMsgs = new Vector<Message>();
		for(int i=0; i<allMessages.size(); i++){
			if(allMessages.get(i).get_msgPosterID()== userId)
				authorMsgs.add(allMessages.get(i));
		}
		return authorMsgs;
	}

	public Vector <Message> searchByContent(String sentence) {
		sentence = sentence.trim();
		//Vector<Message> allMessages = _searchAgent.getSearchTable().Search_getAllMessages();
		Vector<Message> contentMsgs = new Vector<Message>();
		Vector<Long> firstWordMsgIDVec = new Vector<Long>();
		Vector<Long> currWordMsgIDVec;

		Vector <String> sentenceWords = _searchAgent.parseMsg(sentence);
		for(int i = 0 ;i < sentenceWords.size();i++){
			long wordId =_searchAgent.getSearchTable().Search_getWordId(sentenceWords.elementAt(i));
			if(i == 0){//first word
				firstWordMsgIDVec = (Vector <Long>)_searchAgent.getSearchTable().Search_getMessageId(wordId);
			}else{
				currWordMsgIDVec = (Vector <Long>)_searchAgent.getSearchTable().Search_getMessageId(wordId);
				firstWordMsgIDVec.retainAll(currWordMsgIDVec);
			}

		}
		//add the messages from message IDs.
		for(int j = 0;j < firstWordMsgIDVec.size();j++){
			long msgID = firstWordMsgIDVec.get(j).longValue();
			Message currMessage = _searchAgent.getSearchTable().getMessage(msgID);
			contentMsgs.add(currMessage);
		}


		// check if there is \" in the sentence.
		Vector<String> allApostrophed = getAllApostrophed(sentence,numOfCharInString(sentence,'\"'));
		for(int index = 0;index < allApostrophed.size();index++){
			for(int msgIndex = 0;msgIndex < contentMsgs.size();msgIndex++){
				String strPhrase = allApostrophed.get(index);
				String msgBody = contentMsgs.get(msgIndex).get_msgBody().textToDisplay();
				if(!msgBody.contains(strPhrase)){
					contentMsgs.remove(msgIndex);
					msgIndex--;
				}
			}
		}
		return contentMsgs;
	}

	private int numOfCharInString(String str,char ch){
		int count =0;
		for(int i = 0;i<str.length();i++){
			if(str.charAt(i) == ch) count++;
		}
		return count;
	}

	private Vector<String> getAllApostrophed(String sentence, int numOfApostrophes){
		Vector<String> allApostrophed = new Vector<String>();
		int numOfAposPairs = numOfApostrophes/2;
		for(int i=0; i<numOfAposPairs; i++){
			int firstApos = sentence.indexOf('\"');
			sentence = sentence.substring(firstApos+1);
			int secondApos = sentence.indexOf('\"');
			String aposWords = sentence.substring(0, secondApos);
			allApostrophed.add(aposWords);
			if(secondApos < sentence.length()-1) {
				sentence = sentence.substring(secondApos+1);
			}else{
				sentence ="";
				break;
			}

		}
		return allApostrophed;
	}



	public Vector <Message> searchByDate(Date from, Date to) {
		long fromTime = from.getTime();
		long toTime = to.getTime();
		Vector<Message> allMessages = _searchAgent.getSearchTable().Search_getAllMessages();
		Vector<Message> timedMsgs = new Vector<Message>();
		for(int i=0; i<allMessages.size(); i++){
			Message currMsg = allMessages.get(i);
			if((fromTime <= currMsg.get_msgPostTime()) && (toTime >= currMsg.get_msgPostTime()))
				timedMsgs.add(currMsg);
		}
		return timedMsgs;
	}

	/**
	 * Was added only after implementation changed to Compass
	 */
	@Override
	public void removeMessageFromEngine(long mid) {
		// TODO Auto-generated method stub
		
	}

}
