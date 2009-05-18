package searchEngine;

import implementation.Message;


public class SearchAgent {
	public boolean insertMessageToEngine(Message msg){
		String[] msgWords =msg.get_msgBody().textToDisplay().split(" ");
		for(int i=0;i<msgWords.length;i++){
			if(!(msgWords[i].equals(""))){
				//do work
			}
			
		}
		return false;
	}
	
	private String clearWord(String wordToClear){
		char [] whiteChars = {'\\','\"','\t','\n','\b',':', ';','-','=','+','*','.','#'};
		for(int i = 0 ; i < wordToClear.length();i++){
			for(int j = 0;j < whiteChars.length;j++){	
				if((i < (wordToClear.length() - 1)) && (wordToClear.charAt(i)==whiteChars[j])){
					wordToClear = wordToClear.substring(0, i)+wordToClear.substring(i+1, wordToClear.length());
					if(i != 0) i--;
				}else {
					if((wordToClear.charAt(i)==whiteChars[j]) && (i==wordToClear.length()-1)){
						wordToClear = wordToClear.substring(0, wordToClear.length()-1);
						i--;
					}	
				}
			}						
		}
		return wordToClear;
	}
	
}
