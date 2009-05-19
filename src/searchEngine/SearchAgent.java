package searchEngine;

import java.util.Vector;

import domainLayer.PersistenceSystem;

import implementation.Message;


public class SearchAgent {
	
	SearchTable _searchTable;
	
	public SearchAgent(SearchTable searchTable){
		_searchTable = searchTable;
	}
	
	public void insertMessageToEngine(Message msg){
		//parsing the message :
		Vector<String> msgWords = parseMsg(msg.get_msgBody().textToDisplay());
		
		//inserting message words to database :
		for(int i=0; i<msgWords.size(); i++){
			_searchTable.Search_insertWord(msgWords.get(i));
			_searchTable.Search_insertMessageFromWord(msgWords.get(i), msg);
		}
	}
	
	private Vector<String> parseMsg(String msg){
		//removing delimiters :
		char [] whiteChars = {'\\','\"','\t','\n','\b',':', ';','-','=','+','*','.','#'};
		for (int i=0; i<whiteChars.length; i++){
				msg = msg.replace(whiteChars[i], ' ');
		}
		String[] temp = msg.split(" ");
		Vector<String> msgWords = new Vector<String>();
		//removing empty words :
		for(int i=0; i<temp.length; i++){
			if(!temp[i].equals(""))
				msgWords.add(temp[i]);
		}
		return msgWords;
	}
	
}
